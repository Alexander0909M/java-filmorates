package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserValidatorTest {

    @Test
    void shouldThrowIfEmailEmpty() {
        User user = new User();
        user.setEmail("");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldThrowIfEmailNoAt() {
        User user = new User();
        user.setEmail("mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldThrowIfLoginEmpty() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldThrowIfLoginHasSpaces() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("bad login");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    void shouldThrowIfBirthdayInFuture() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }
}
