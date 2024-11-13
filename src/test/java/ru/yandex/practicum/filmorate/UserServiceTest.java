package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserService userService;
    private User user;


    @BeforeEach
    public void userCreate() {
        user = new User(1L,"edsa@mail.ru", "LOGIN", "DADSA", LocalDate.of(2000,10,10));
    }

    @Test
    public void testCreatingUser() {
        UserDto dto = userService.createUser(user);
        assertThat(dto)
                .hasFieldOrPropertyWithValue("email", "edsa@mail.ru");
        assertThat(dto)
                .hasFieldOrPropertyWithValue("login", "LOGIN");
        assertThat(dto)
                .hasFieldOrPropertyWithValue("name", "DADSA");
    }

    @Test
    public void testCreatingUserNoName() {
        user.setName("");
        UserDto dto = userService.createUser(user);
        assertThat(dto)
                .hasFieldOrPropertyWithValue("email", "edsa@mail.ru");
        assertThat(dto)
                .hasFieldOrPropertyWithValue("login", "LOGIN");
        assertThat(dto)
                .hasFieldOrPropertyWithValue("name", "LOGIN");
    }
}
