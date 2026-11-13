package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    public Film getFilmById(int id) {
        Film film = filmStorage.findById(id);
        if (film == null) {
            throw new ValidationException("Фильм с id " + id + " не найден");
        }
        return film;
    }

    public Film createFilm(Film film) {
        FilmValidator.validate(film);
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        if (film.getId() == 0) {
            throw new ValidationException("Id должен быть указан");
        }
        if (filmStorage.findById(film.getId()) == null) {
            throw new ValidationException("Фильм с id " + film.getId() + " не найден");
        }
        FilmValidator.validate(film);
        return filmStorage.update(film);
    }

    public Film addLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        if (userStorage.findById(userId) == null) {
            throw new ValidationException("Пользователь с id " + userId + " не найден");
        }
        film.getLikes().add(userId);
        return film;
    }

    public Film removeLike(int filmId, int userId) {
        Film film = getFilmById(filmId);
        if (userStorage.findById(userId) == null) {
            throw new ValidationException("Пользователь с id " + userId + " не найден");
        }
        film.getLikes().remove(userId);
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}