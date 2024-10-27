package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

@RestController
@Component
@RequestMapping("/films")
public class FilmController {
    private InMemoryFilmStorage filmStorage;
    private FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> showAll() {
        return filmStorage.showAll();
    }

    @GetMapping("/{id}")
    public Film showFilmById(@PathVariable Long id) {
        return filmStorage.showFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularList(@RequestParam Optional<Integer> count) {
        return filmService.getPopularList(count);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film leaveALike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.leaveALike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteALike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.deleteALike(id, userId);
    }
}
