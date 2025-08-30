package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.http.BadRequestException;
import ru.yandex.practicum.filmorate.exception.storage.NotExistedModelException;
import ru.yandex.practicum.filmorate.response.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notExistedModelHandler(NotExistedModelException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestHandler(BadRequestException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidJsonHandler(HttpMessageNotReadableException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse runtimeErrorHandler(RuntimeException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
