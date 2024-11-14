package ru.yandex.practicum.filmorate.dal.UserRerositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.dal.UserRerositories.requests.UserRequests.*;

@Repository("UserRepository")
public class UserRepository extends BaseRepository<User> {
    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY.query);
    }

    public Optional<User> findById(long userId) {
        return findOne(FIND_BY_ID_QUERY.query, userId);
    }

    public Optional<User> findByEmail(String email) {
        return findOne(FIND_BY_EMAIL_QUERY.query, email);
    }

    public User save(User user) {
        long id = insert(
                INSERT_QUERY.query,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday())
        );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(
                UPDATE_QUERY.query,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId()
        );
        return user;
    }

    public boolean delete(long userId) {
        return delete(DELETE_BY_ID_QUERY.query, userId);
    }
}
