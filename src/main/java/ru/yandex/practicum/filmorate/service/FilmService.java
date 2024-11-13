package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepositories.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenresDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenresMapper;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.utils.Genres;
import ru.yandex.practicum.filmorate.model.utils.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmRepository filmRepository;
    private final MpaRepository mpaRepository;
    private final FilmsGenresRepository filmsGenresRepository;
    private final GenresRepository genresRepository;
    private final UserService userService;
    private final LikesRepository likesRepository;

    @Autowired
    public FilmService(@Qualifier("FilmRepository") FilmRepository filmRepository, MpaRepository mpaRepository, FilmsGenresRepository filmsGenresRepository, GenresRepository genresRepository, UserService userService, LikesRepository likesRepository) {
        this.filmRepository = filmRepository;
        this.mpaRepository = mpaRepository;
        this.filmsGenresRepository = filmsGenresRepository;
        this.genresRepository = genresRepository;
        this.userService = userService;
        this.likesRepository = likesRepository;
    }

    public List<FilmDto> getFilms() {
        return filmRepository.findAll()
                .stream()
                .map(this::setRating)
                .map(this::findGenres)
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto getFilmById(long filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с идентификатором " + filmId + " не найден."));
        setRating(film);
        findGenres(film);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto createFilm(Film request) {
        try {
            checkFilm(request);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        setRating(request);
        setGenres(request);
        request = filmRepository.save(request);
        if (request.getGenres() != null) {
            filmsGenresRepository.save2(request.getId(), checkGenreListDuplicates(request.getGenres()));
        }
        return FilmMapper.mapToFilmDto(request);
    }

    public FilmDto updateFilm(Film film) {
        try {
            checkFilm(film);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        FilmDto serviceOldFilm = getFilmById(film.getId());

        if (serviceOldFilm.getGenres() != null && !serviceOldFilm.getGenres().isEmpty()) {
            filmsGenresRepository.delete(film.getId());
        }
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            filmsGenresRepository.save2(film.getId(), film.getGenres());
        }
        setRating(film);
        setGenres(film);
        filmRepository.update(film);
        log.info("Фильм {} был успешно обновлен", film);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto deleteFilm(long filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с идентификатором " + filmId + " не найден."));

        if (!filmRepository.delete(filmId)) {
            log.error("Ошибка удаления");
            throw new ValidateException("Ошибка при удалении фильма с ИД " + filmId);
        }
        return FilmMapper.mapToFilmDto(film);
    }

    public Film checkFilm(Film request) {
        if (request.getDescription().length() > 200) {
            throw new ValidateException("Превышен лимит символов");
        }
        if (request.getReleaseDate() == null || request.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateException("Дата релиза не должна быть позже 28 декабря 1895 года ");
        }
        if (!(request.getDuration() > 0)) {
            throw new ValidateException("Продолжительность должна положительным числом");
        }
        if (request.getName().isEmpty() || request.getName().isBlank()) {
            throw new ValidateException("Название не может быть пустым");
        }
        return request;
    }

    public Film setGenres(Film film) {
        if (film.getGenres() != null) {
            List<Genres> genres = film.getGenres();
            genres = genresRepository.getListGenre(genres);
            if (genres.isEmpty()) {
                log.error("Введен несуществующий жанр");
                throw new ValidateException("Жанр с таким id не существует");
            }
            film.setGenres(genres);
            return film;
        }
        return film;
    }

    public Film findGenres(Film film) {
        List<Genres> genres = filmsGenresRepository.getFilmGenres(film.getId());
        if (genres != null && !genres.isEmpty()) {
            film.setGenres(genres);
        }
        return film;
    }

    public Film setRating(Film film) {
        if (film.getMpa() != null) {
            Optional<Mpa> oMpa = mpaRepository.findById(film.getMpa().getId());
            if (oMpa.isPresent()) {
                film.setMpa(oMpa.get());
            } else {
                throw new ValidateException("Фильм с идентификатором " + film.getId() + " не найден.");
            }
        }
        return film;
    }

    public List<MpaDto> findAllMpa() {
        return mpaRepository.findAll().stream()
                .map(MpaMapper::mapToMpaDto)
                .collect(Collectors.toList());
    }

    public MpaDto findMpaById(long id) {
        if (mpaRepository.findById(id).isEmpty()) {
            log.error("Неверный id мпа");
            throw new NotFoundException("Неверный id mpa");
        }
        return MpaMapper.mapToMpaDto(mpaRepository.findById(id).get());
    }

    public List<GenresDto> findAllGenres() {
        return genresRepository.findAll().stream()
                .map(GenresMapper::mapToGenresDto)
                .collect(Collectors.toList());
    }

    public GenresDto findGenreById(long id) {
        if (genresRepository.findById(id).isEmpty()) {
            log.error("Неверный id жанра");
            throw new NotFoundException("Неверный id жанра");
        }
        return GenresMapper.mapToGenresDto(genresRepository.findById(id).get());
    }

    public void addLike(long filmId, long userId) {
        userService.checkUserById(userId);
        likesRepository.addLike(filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        userService.checkUserById(userId);
        likesRepository.deleteLike(filmId, userId);
    }

    public List<FilmDto> findPopularFilm(String count) {
        return filmRepository.findPopularFilm(count)
                .stream()
                .map(this::setRating)
                .map(this::setGenres)
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public List<Genres> checkGenreListDuplicates(List<Genres> genresList) {
        Map<Long, Genres> uniqueGenresMap = genresList.stream()
                .collect(Collectors.toMap(Genres::getId, genre -> genre, (existing, replacement) -> existing));
        return uniqueGenresMap.values().stream().collect(Collectors.toList());
    }
}
