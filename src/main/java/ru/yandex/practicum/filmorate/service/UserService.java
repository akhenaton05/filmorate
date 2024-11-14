package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRerositories.FriendsRepository;
import ru.yandex.practicum.filmorate.dal.UserRerositories.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.utils.Status;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;

    @Autowired
    public UserService(@Qualifier("UserRepository") UserRepository userRepository, FriendsRepository friendsRepository) {
        this.userRepository = userRepository;
        this.friendsRepository = friendsRepository;
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден."));
        return UserMapper.mapToUserDto(user);
    }

    public UserDto createUser(User request) {
        try {
            checkUser(request);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        Optional<User> alreadyExistUser = userRepository.findByEmail(request.getEmail());
        if (alreadyExistUser.isPresent()) {
            throw new DuplicatedDataException("Данный email уже используется");
        }
        User user = UserMapper.mapToUser(request);
        user = userRepository.save(user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto updateUser(User user) {
        try {
            checkUser(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        userRepository.update(user);
        log.info("Пользователь {} был успешно обновлен", user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto deleteUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден."));
        if (!userRepository.delete(userId)) {
            log.error("Ошибка удаления");
            throw new ValidateException("Ошибка при удалении пользователя с ИД " + userId);
        }
        return UserMapper.mapToUserDto(user);
    }

    public List<UserDto> addFriend(long id, long friendId) {
        if (id == friendId) {
            log.error("id пользователей совпадают");
            throw new ValidationException("Указанные id совпадают");
        }
        try {
            checkUserById(id);
            checkUserById(friendId);
        } catch (Exception e) {
            log.error("Ошибка проверки ID пользователей");
            throw e;
        }
        if (notFriendsCheck(id, friendId)) {
            friendsRepository.insertFriend(id, friendId, Status.UNCONFIRMED.toString());
        } else {
            log.error("ID друга уже есть в БД");
            throw new ValidationException("Пользователь с id " + friendId +
                    " уже есть в списке друзей id " + id);
        }
        if (!notFriendsCheck(friendId, id)) {
            friendsRepository.updateFriendsStatus(friendId, id, Status.CONFIRMED.toString());
            friendsRepository.updateFriendsStatus(id, friendId, Status.CONFIRMED.toString());
        }
        return getUsersById(friendsRepository.getFriendsById(id));
    }

    public List<UserDto> showUserFriends(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + id + " не найден."));
        return getUsersById(friendsRepository.getFriendsById(id));
    }

    public List<UserDto> deleteFriend(long id, long friendId) {
        try {
            checkUserById(id);
            checkUserById(friendId);
        } catch (Exception e) {
            log.error("Ошибка проверки ID пользователей");
            throw e;
        }
        if (notFriendsCheck(id, friendId)) {
            log.error("Пользователи не являются друзьями");
            return getUsersById(friendsRepository.getFriendsById(id));
        }
        friendsRepository.deleteFriend(id, friendId);
        if (!notFriendsCheck(friendId, id)) {
            friendsRepository.updateFriendsStatus(friendId, id, Status.UNCONFIRMED.toString());
        }
        return getUsersById(friendsRepository.getFriendsById(id));
    }

    public List<UserDto> showMututalFriends(long id, long friendId) {
        try {
            checkUserById(id);
            checkUserById(friendId);
        } catch (Exception e) {
            log.error("Ошибка проверки ID пользователей");
            throw e;
        }
        return getUsersById(friendsRepository.getMutualFriends(id, friendId));
    }

    public void checkUserById(long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " отсутствует");
        }
    }

    public boolean notFriendsCheck(long id, long friendId) {
        return friendsRepository.checkFriendship(id, friendId).isEmpty();
    }


    public User checkUser(User request) {
        if (request.getEmail() == null || !request.getEmail().contains("@")) {
            throw new ValidateException("Неверный формат email");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            request.setName(request.getLogin());
        }
        if (request.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("Дата рождения введена неверно");
        }
        return request;
    }

    private List<UserDto> getUsersById(List<Long> idsList) {
        return idsList.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }
}
