package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);
    @Getter
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> showAll() {
        return users.values();
    }

    public User showUserById(Long id) {
        if (!users.containsKey(id)){
            throw new NotFoundException("Пользователя с ID " + id + " нету");
        }
        log.info("Пользователь c ID {} найден", id);
        return users.get(id);
    }

    public User addUser(User user) {
        User newUser;
        try {
            newUser = checkUser(user);
        } catch (ValidateException e) {
            log.error(e.getMessage());
            throw e;
        }
        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        user.setFriendsList(new HashSet<>());
        user.setLikesList(new HashSet<>());
        log.info("Пользователь {} был успешно добавлен", newUser);
        return newUser;
    }

    public User updateUser(User user) {
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
        throw new NotFoundException("User с ID " + updatedUser.getId() + " не найден");
    }

    public User checkUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidateException("Неверный формат email");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("Дата рождения введена неверно");
        }
        return user;
    }

    public User getUserById(Long id) {
        if (users.containsKey(id)){
            return users.get(id);
        }
        throw new NotFoundException("Юзера с ID " + id + " несуществует");
    }

    public long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
