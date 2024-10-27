package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@RestController
@Component
@RequestMapping("/users")
public class UserController {
    private InMemoryUserStorage userStorage;
    private UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> showAll() {
        return userStorage.showAll();
    }

    @GetMapping("/{id}")
    public User showUserById(@PathVariable Long id) {
        return userStorage.showUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> showAllFriends(@PathVariable Long id) {
        return userService.showAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> showAllMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.showAllMutualFriends(id, otherId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userStorage.addUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.addFriend(id, friendId);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.deleteFriend(id, friendId);
    }
}
