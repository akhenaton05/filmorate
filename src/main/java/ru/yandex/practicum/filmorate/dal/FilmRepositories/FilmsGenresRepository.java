package ru.yandex.practicum.filmorate.dal.FilmRepositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.utils.Genres;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.dal.FilmRepositories.requests.FilmGenresRequests.*;

@Repository
public class FilmsGenresRepository extends BaseRepository<Genres> {
    public FilmsGenresRepository(JdbcTemplate jdbc, RowMapper<Genres> mapper) {
        super(jdbc, mapper);
    }

    public List<Genres> getFilmGenres(Long filmId) {
        return findMany(FIND_FILMS_GENRES_QUERY.query, filmId);
    }

    public boolean delete(long filmId) {
        return delete(DELETE_QUERY.query, filmId);
    }

    public void save2(Long filmId, List<Genres> uniqueGenres) {
        int size = uniqueGenres.size();
        List<String> queries = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Long genreId = uniqueGenres.get(i).getId();
            queries.add("INSERT INTO films_genres (film_id, genre_id) " +
                    "SELECT " + filmId + ", " + genreId + " FROM DUAL WHERE NOT EXISTS (" +
                    "SELECT 1 FROM films_genres WHERE film_id = " + filmId + " AND genre_id = " + genreId + ")");
        }
        String[] arr = queries.toArray(new String[queries.size()]);
        insertBatch(arr);
    }
}
