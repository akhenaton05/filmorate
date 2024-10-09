package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film leaveALike(Long id, Long userId) {
        Film film = filmStorage.showFilmById(id);
        User user = userStorage.getUserById(userId);
        user.getLikesList().add(id);
        film.setLikesCount(film.getLikesCount() + 1);
        log.info("Фильм с ID {} понравился пользователю {}", id, userId);
        return film;
    }

    public Film deleteALike(Long id, Long userId) {
        Film film = filmStorage.showFilmById(id);
        User user = userStorage.getUserById(userId);
        user.getLikesList().remove(id);
        film.setLikesCount(film.getLikesCount() - 1);
        log.info("Фильм с ID {} удален из понравившихся пользователем {}", id, userId);
        return film;
    }

    public Collection<Film> getPopularList(Optional<Integer> count) {
        return count.map(integer -> filmStorage.showAll().stream()
                .sorted((o1, o2) -> o2.getLikesCount() - o1.getLikesCount())
                .limit(integer).toList())
                .orElseGet(() -> filmStorage.showAll().stream()
                        .limit(10).toList());
    }
}
