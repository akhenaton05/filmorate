package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FilmRepositories.FilmRepository;
import ru.yandex.practicum.filmorate.dal.UserRerositories.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({UserRepository.class, UserRowMapper.class})
public class UserRepositoryTest {
    private UserRepository userRepository;
    private User user;

    @Autowired
    public UserRepositoryTest(@Qualifier("UserRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    public void userCreate() {
        user = new User(1L,"edsa@mail.ru", "LOGIN", "DADSA", LocalDate.of(2000,10,10));
    }

    @Test
    public void testCreatingUser() {
        User created = userRepository.save(user);
        assertThat(created).hasFieldOrPropertyWithValue("id", created.getId());
    }

    @Test
    public void testFindingUserById() {
        User created = userRepository.save(user);

        Optional<User> searchFilm = userRepository.findById(created.getId());
        assertThat(searchFilm)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", created.getId()));
    }
}
