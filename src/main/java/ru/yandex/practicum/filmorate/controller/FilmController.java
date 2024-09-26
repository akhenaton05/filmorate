package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.utils.FilmUtils;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@RestController
@Component
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    private FilmUtils filmUtils;

    @GetMapping
    public Collection<Film> showAll() {
        return filmUtils.getFilms().values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        Film newFilm;
        try {
            newFilm = filmUtils.checkFilm(film);
        } catch (ValidateException e) {
            log.error(e.getMessage());
            throw e;
        }
        newFilm.setId(filmUtils.getNextId());
        filmUtils.getFilms().put(newFilm.getId(), newFilm);
        log.info("Фильм {} был успешно добавлен", newFilm);
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film updatedFilm;
        try {
            updatedFilm = filmUtils.checkFilm(film);
        } catch (ValidateException e) {
            log.error(e.getMessage());
            throw e;
        }
        if (updatedFilm.getId() == null) {
            throw new ValidateException("ID фильма должен быть указан");
        }
        if (filmUtils.getFilms().containsKey(updatedFilm.getId())) {
            Film oldFilm = filmUtils.getFilms().get(updatedFilm.getId());
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
}
