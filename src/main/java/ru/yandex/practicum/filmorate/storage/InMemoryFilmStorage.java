package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    @Getter
    private final Map<Long, Film> films = new HashMap<>();

    public Collection<Film> showAll() {
        return films.values();
    }

    public Film showFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильма с ID " + id + "нет");
        }
        log.info("Фильм c ID {} найден", id);
        return films.get(id);
    }

    public Film addFilm(Film film) {
        Film newFilm;
        try {
            newFilm = checkFilm(film);
        } catch (ValidateException e) {
            log.error(e.getMessage());
            throw e;
        }
        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        log.info("Фильм {} был успешно добавлен", newFilm);
        return newFilm;
    }

    public Film updateFilm(Film film) {
        Film updatedFilm;
        try {
            updatedFilm = checkFilm(film);
        } catch (ValidateException e) {
            log.error(e.getMessage());
            throw e;
        }
        if (updatedFilm.getId() == null) {
            throw new ValidateException("ID фильма должен быть указан");
        }
        if (films.containsKey(updatedFilm.getId())) {
            Film oldFilm = films.get(updatedFilm.getId());
            if (oldFilm.equals(updatedFilm)) {
                log.info("Фильм {} идентичен с обновляемым", updatedFilm);
                return oldFilm;
            }
            oldFilm.setName(updatedFilm.getName());
            oldFilm.setDescription(updatedFilm.getDescription());
            oldFilm.setReleaseDate(updatedFilm.getReleaseDate());
            oldFilm.setDuration(updatedFilm.getDuration());
            log.info("Фильм {} был успешно обновлен", oldFilm);
            return oldFilm;
        }
        throw new NotFoundException("Film с ID " + updatedFilm.getId() + " не найден");
    }

    public Film checkFilm(Film film) {
        if (film.getDescription().length() > 200) {
            throw new ValidateException("Превышен лимит символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateException("Дата релиза не должна быть позже 28 декабря 1895 года ");
        }
        if (!(film.getDuration()>0)) {
            throw new ValidateException("Продолжительность должна положительным числом");
        }
        return film;
    }

    public long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
