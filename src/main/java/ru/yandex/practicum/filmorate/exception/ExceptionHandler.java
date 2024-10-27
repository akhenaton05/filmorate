package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler extends RuntimeException {

    public ErrorResponse validationHandler(ValidateException e) {
        return new ErrorResponse("error:", e.getMessage());
    }

    public ErrorResponse notFoundHandler(ValidateException e) {
        return new ErrorResponse("error:", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse defaultHandler(Exception e) {
        return new ErrorResponse("error:", e.getMessage());
    }
}
