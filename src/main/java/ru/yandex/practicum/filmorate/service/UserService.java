package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.findAll();
    }

    public User getUserById(int id) {
        User user = userStorage.findById(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        return user;
    }

    public User createUser(User user) {
        UserValidator.validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        if (user.getId() == 0) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        if (userStorage.findById(user.getId()) == null) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        UserValidator.validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.update(user);
    }

    public User addFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return user;
    }

    public User removeFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        return user;
    }

    public List<User> getFriends(int userId) {
        User user = getUserById(userId);
        Set<Integer> friendIds = user.getFriends();
        return friendIds.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        User user = getUserById(userId);
        User other = getUserById(otherId);
        return user.getFriends().stream()
                .filter(other.getFriends()::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }
}