package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.utils.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenresRowMapper implements RowMapper<Genres> {
    @Override
    public Genres mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Genres genres = new Genres();
        genres.setId(resultSet.getLong("genre_id"));
        genres.setName(resultSet.getString("name"));

        return genres;
    }
}
