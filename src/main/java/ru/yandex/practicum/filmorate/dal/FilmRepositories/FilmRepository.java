package ru.yandex.practicum.filmorate.dal.FilmRepositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository("FilmRepository")
public class FilmRepository extends BaseRepository<Film> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films (name, description, release_date, duration, likes_count, rating_id) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, likes_count = ?, rating_id = ? " +
            "WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String POPULAR_FILM_QUERY = "SELECT * FROM films f JOIN (SELECT film_id, COUNT(user_id) AS like_count" +
            " FROM users_likes GROUP BY film_id ORDER BY like_count DESC) l ON f.id=l.film_id LIMIT ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Film> findById(long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    public Film save(Film film) {
        Long rating = null;
        if (film.getMpa() != null) {
            rating = film.getMpa().getId();
        }
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getRate(),
                rating
        );
        film.setId(id);
        return film;
    }

    public Film update(Film film) {
        Long rating = null;
        if (film.getMpa() != null) {
            rating = film.getMpa().getId();
        }
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getRate(),
                rating,
                film.getId()
        );
        return film;
    }

    public boolean delete(long filmId) {
        return delete(DELETE_BY_ID_QUERY, filmId);
    }

    public List<Film> findPopularFilm(String count) {
        return findMany(POPULAR_FILM_QUERY, Integer.parseInt(count));
    }
}
