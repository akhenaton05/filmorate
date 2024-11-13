package ru.yandex.practicum.filmorate.dal.FilmRepositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.utils.Mpa;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.dal.FilmRepositories.requests.MpaRequests.*;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> findAll() {
        return findMany(FIND_ALL_QUERY.query);
    }

    public Optional<Mpa> findById(long id) {
        return findOne(FIND_BY_ID_QUERY.query, id);
    }
}
