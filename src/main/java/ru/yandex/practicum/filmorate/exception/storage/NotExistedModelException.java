package ru.yandex.practicum.filmorate.exception.storage;

public class NotExistedModelException extends RuntimeException {
  public NotExistedModelException(String message) {
    super(message);
  }
}
