package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получен запрос на получение всех фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Получен запрос на создание фильма: {}", film);
        try {
            FilmValidator.validate(film);
            film.setId(currentId++);
            films.put(film.getId(), film);
            log.info("Фильм успешно создан с id={}", film.getId());
            return film;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при создании фильма: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен запрос на обновление фильма: {}", film);
        try {
            if (film.getId() == 0) {
                throw new ValidationException("Id должен быть указан");
            }
            if (!films.containsKey(film.getId())) {
                throw new ValidationException("Фильм с id " + film.getId() + " не найден");
            }
            FilmValidator.validate(film);
            films.put(film.getId(), film);
            log.info("Фильм успешно обновлен с id={}", film.getId());
            return film;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении фильма: {}", e.getMessage());
            throw e;
        }
    }
}