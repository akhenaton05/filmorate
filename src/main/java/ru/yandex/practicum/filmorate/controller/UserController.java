package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.utils.UserUtils;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserUtils userUtils;

    @GetMapping
    public Collection<User> showAll() {
        return userUtils.getUsers().values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        User newUser;
        try {
            newUser = userUtils.checkUser(user);
        } catch (ValidateException e) {
            log.error(e.getMessage());
            throw e;
        }
        newUser.setId(userUtils.getNextId());
        userUtils.getUsers().put(newUser.getId(), newUser);
        log.info("Пользователь {} был успешно добавлен", newUser);
        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        User updatedUser;
        try {
            updatedUser = userUtils.checkUser(user);
        } catch (ValidateException e) {
            log.error(e.getMessage());
            throw e;
        }
        if (updatedUser.getId() == null) {
            throw new ValidateException("ID пользователя должен быть указан");
        }
        if (userUtils.getUsers().containsKey(updatedUser.getId())) {
            User oldUser = userUtils.getUsers().get(updatedUser.getId());
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
}
