package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> showAll() {
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        User newUser;
        try {
            newUser = checkUser(user);
        } catch (ValidateException e) {
            log.error(e.getMessage());
            throw e;
        }
        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        log.info("Пользователь {} был успешно добавлен", newUser);
        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        User updatedUser;
        try {
            updatedUser = checkUser(user);
        } catch (ValidateException e) {
            log.error(e.getMessage());
            throw e;
        }
        if (updatedUser.getId() == null) {
            throw new ValidateException("ID пользователя должен быть указан");
        }
        if (users.containsKey(updatedUser.getId())) {
            User oldUser = users.get(updatedUser.getId());
            if (oldUser.equals(updatedUser)) {
                log.info("Обновляемые данные идентичны старым");
                return oldUser;
            }
            oldUser.setName(updatedUser.getName());
            oldUser.setEmail(updatedUser.getEmail());
            oldUser.setLogin(updatedUser.getLogin());
            oldUser.setBirthday(updatedUser.getBirthday());
            log.info("Пользователь {} был успешно обновлен", oldUser);
            return oldUser;
        }
        throw new ValidateException("User с ID " + updatedUser.getId() + " не найден");
    }

    private User checkUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidateException("Неверный формат email");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Пустое поле Name было изменено на логин {}", user.getLogin());
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("Дата рождения введена неверно");
        }
        log.info("Пользователь прошел проверку полей");
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
