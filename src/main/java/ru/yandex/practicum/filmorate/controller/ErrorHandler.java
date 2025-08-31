package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.http.BadRequestException;
import ru.yandex.practicum.filmorate.exception.storage.NotExistedModelException;
import ru.yandex.practicum.filmorate.response.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notExistedModelHandler(NotExistedModelException exception) {
        logExceptionInfo("Model wasn't found. {}: {}.", exception);
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse notValidArgumentHandler(MethodArgumentNotValidException exception) {
        return handleInvalidIncomeData(exception);
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestHandler(BadRequestException exception) {
        return handleInvalidIncomeData(exception);
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidJsonHandler(HttpMessageNotReadableException exception) {
        return handleInvalidIncomeData(exception);
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidJsonHandler(ConstraintViolationException exception) {
        return handleInvalidIncomeData(exception);
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse runtimeErrorHandler(RuntimeException exception) {
        log.warn("Unexpected error. {}.", exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    public void logExceptionInfo(String pattern, Exception exception) {
        log.info(
                pattern,
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );
    }

    public ErrorResponse handleInvalidIncomeData(Exception exception) {
        logExceptionInfo("Invalid income data. {}: {}.", exception);
        return new ErrorResponse(exception.getMessage());
    }
}
