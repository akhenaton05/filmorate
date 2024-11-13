package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenresDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Component
public class FilmController {
    @Autowired
    private FilmService filmService;

    @GetMapping("/films")
    @ResponseStatus(HttpStatus.OK)
    public List<FilmDto> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/films/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto getFilmById(@PathVariable("filmId") long filmId) {
        return filmService.getFilmById(filmId);
    }

    @PostMapping("/films")
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto createFilm(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping("/films")
    public FilmDto updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/films/{filmId}")
    public FilmDto deleteFilm(@PathVariable("filmId") long filmId) {
        return filmService.deleteFilm(filmId);
    }

    @GetMapping("/mpa")
    public List<MpaDto> findAllMpa() {
        return filmService.findAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public MpaDto findMpaById(@PathVariable long id) {
        return filmService.findMpaById(id);
    }

    @GetMapping("/genres")
    public List<GenresDto> findAllGenres() {
        return filmService.findAllGenres();
    }

    @GetMapping("/genres/{id}")
    public GenresDto findGenreById(@PathVariable long id) {
        return filmService.findGenreById(id);
    }

    @PutMapping("/films/{id}/like/{userId}") //поставить лайк
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<FilmDto> findPopularFilm(@RequestParam(defaultValue = "10") String count) {
        return filmService.findPopularFilm(count);
    }
}
