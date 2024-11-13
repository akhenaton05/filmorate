package ru.yandex.practicum.filmorate.dal.FilmRepositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.utils.Genres;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FilmsGenresRepository extends BaseRepository<Genres> {
    private static final String DELETE_QUERY = "DELETE FROM films_genres WHERE film_id = ?";
    private static final String FIND_FILMS_GENRES_QUERY = "SELECT g.genre_id, g.name FROM films_genres f JOIN genres g ON f.genre_id=g.genre_id WHERE f.film_id = ?";

    public FilmsGenresRepository(JdbcTemplate jdbc, RowMapper<Genres> mapper) {
        super(jdbc, mapper);
    }

    public List<Genres> getFilmGenres(Long filmId) {
        return findMany(FIND_FILMS_GENRES_QUERY, filmId);
    }

    public boolean delete(long filmId) {
        return delete(DELETE_QUERY, filmId);
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
