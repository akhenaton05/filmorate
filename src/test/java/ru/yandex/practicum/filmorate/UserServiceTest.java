package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
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
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan(basePackages = "ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserService userService;
    private User user;
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();


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

    @Test
    public void testCreatingUserWrongEmail() {
        user.setEmail("dsaa");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        List<String> validMessages =
                List.of("должно иметь формат адреса электронной почты");
        if (!violations.isEmpty()) {
            for (ConstraintViolation<User> violation : violations) {
                assertTrue(validMessages.contains(violation.getMessage()),
                        "Сообщение об ошибке не соответствует " + violation.getMessage());
            }
        }
    }




}
