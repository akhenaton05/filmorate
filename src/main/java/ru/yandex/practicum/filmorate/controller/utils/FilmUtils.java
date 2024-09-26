package ru.yandex.practicum.filmorate.controller.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class FilmUtils {
    @Getter
    private final Map<Long, Film> films = new HashMap<>();

    public Film checkFilm(Film film) {
        if (film.getDescription().length() > 200) {
            throw new ValidateException("Превышен лимит символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateException("Дата релиза не должна быть позже 28 декабря 1895 года ");
        }
        if (!(film.getDuration().isPositive())) {
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
