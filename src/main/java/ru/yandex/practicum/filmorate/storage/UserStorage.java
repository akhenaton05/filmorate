package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> showAll();

    User showUserById(Long id);

    User addUser(User user);

    User updateUser(User user);

    User checkUser(User user);

    User getUserById(Long id);

    long getNextId();
}
