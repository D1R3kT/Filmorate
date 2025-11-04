package ru.yandex.practicum.filmorate.exceptions;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFoundException(final NotFoundException exp) {
        return Map.of("error", exp.getMessage());
    }

    @ExceptionHandler
    public Map<String, String> validateException(final ValidationException exp) {
        return Map.of("error", exp.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> conditionMetException(final ConditionsMetException exp) {
        return Map.of("error", exp.getMessage());
    }

}
