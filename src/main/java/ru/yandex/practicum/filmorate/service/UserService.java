package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(long id, long friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        if (user.getFriendsList().contains(friendId)){
            log.error("Пользователь с ID {} уже добавлен в друзья", friendId);
            throw new ValidateException("Пользователь с ID " + friendId + " уже добавлен в друзья");
        }
        user.getFriendsList().add(friend.getId());
        friend.getFriendsList().add(user.getId());
        log.info("Пользователь с ID {} успешно добавлен в друзья пользователя {}", friendId, id);
        return user;
    }

    public User deleteFriend(Long id, Long friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        if (user.getFriendsList().contains(friendId)){
            user.getFriendsList().remove(friendId);
            friend.getFriendsList().remove(id);
        }
        log.info("Пользователь с ID {} удален из друзей пользователя {}", friendId, id);
        return user;
    }

    public Collection<User> showAllFriends(long id) {
        return userStorage.getUserById(id).getFriendsList().stream()
                .map(uId -> userStorage.getUserById(uId))
                .toList();
    }

    public Collection<User> showAllMutualFriends(long id, long otherId) {
        List<User> userOneFriends = userStorage.getUserById(id).getFriendsList().stream()
                .map(uId -> userStorage.getUserById(uId))
                .toList();
        List<User> userTwoFriends = userStorage.getUserById(otherId).getFriendsList().stream()
                .map(uId -> userStorage.getUserById(uId))
                .toList();
        List<User> result = userOneFriends.stream()
                .filter(userTwoFriends::contains)
                .toList();
        if (result.isEmpty()) {
            log.error("Общих друзей нету");
            throw new NotFoundException("Общих друзей нету");
        }
        return result;
    }
}
