package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> showAll() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
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

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
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
        throw new ValidateException("Film с ID " + updatedFilm.getId() + " не найден");
    }

    private Film checkFilm(Film film) {
        if (film.getDescription().length() > 200) {
            throw new ValidateException("Превышен лимит символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateException("Дата релиза не должна быть позже 28 декабря 1895 года ");
        }
        if (!(film.getDuration().isPositive())) {
            throw new ValidateException("Продолжительность должна положительным числом");
        }
        log.info("Фильм прошел проверку полей");
        return film;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
