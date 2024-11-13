package ru.yandex.practicum.filmorate.dal.FilmRepositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.utils.Genres;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class GenresRepository extends BaseRepository<Genres> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE genre_id = ?";

    public GenresRepository(JdbcTemplate jdbc, RowMapper<Genres> mapper) {
        super(jdbc, mapper);
    }

    public List<Genres> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Genres> findById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public List<Genres> getListGenre(List<Genres> list) {
        String placeholders = String.join(",", Collections.nCopies(list.size(), "?"));
        String listGenreQuery = "SELECT genre_id, name FROM genres WHERE genre_id IN (" + placeholders + ")";
        List<Long> listInt = list.stream()
                .map(Genres::getId)
                .toList();
        Object[] params = listInt.toArray(new Object[0]);
        return findMany(listGenreQuery, params);
    }
}