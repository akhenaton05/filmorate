package ru.yandex.practicum.filmorate.dal.FilmRepositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.Film;

@Repository
public class LikesRepository extends BaseRepository<Film> {
    private static final String INSERT_LIKE_QUERY = "INSERT INTO users_likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM users_likes WHERE film_id = ? AND user_id = ?";

    public LikesRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }


    public void addLike(long filmId, long userId) {
        insert(
                INSERT_LIKE_QUERY,
                filmId,
                userId
        );
    }

    public void deleteLike(long filmId, long userId) {
        update(
                DELETE_LIKE_QUERY,
                filmId,
                userId
        );
    }
}