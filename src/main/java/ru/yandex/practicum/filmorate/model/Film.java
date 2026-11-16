package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Integer> likes = new HashSet<>();

    @ControllerAdvice
    @ResponseBody
    public static class ErrorHandler {

        @ExceptionHandler(ValidationException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ru.yandex.practicum.filmorate.model.ErrorResponse handleValidationException(ValidationException ex) {
            return new ru.yandex.practicum.filmorate.model.ErrorResponse(ex.getMessage());
        }

        @ExceptionHandler(NotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ru.yandex.practicum.filmorate.model.ErrorResponse handleNotFoundException(NotFoundException ex) {
            return new ru.yandex.practicum.filmorate.model.ErrorResponse(ex.getMessage());
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ru.yandex.practicum.filmorate.model.ErrorResponse handleException(Exception ex) {
            return new ru.yandex.practicum.filmorate.model.ErrorResponse("Внутренняя ошибка сервера");
        }
    }
}
