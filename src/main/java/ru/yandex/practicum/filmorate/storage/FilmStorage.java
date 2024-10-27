package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> showAll();

    Film showFilmById(Long id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film checkFilm(Film film);

    long getNextId();
}
