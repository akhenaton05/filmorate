package ru.yandex.practicum.filmorate.dal.FilmRepositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.Film;

import static ru.yandex.practicum.filmorate.dal.FilmRepositories.requests.LikesRequests.*;

@Repository
public class LikesRepository extends BaseRepository<Film> {
    public LikesRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }


    public void addLike(long filmId, long userId) {
        insert(
                INSERT_LIKE_QUERY.query,
                filmId,
                userId
        );
    }

    public void deleteLike(long filmId, long userId) {
        update(
                DELETE_LIKE_QUERY.query,
                filmId,
                userId
        );
    }
}