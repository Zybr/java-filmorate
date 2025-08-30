package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(
            Film film,
            User user
    ) {
        film.getLikes()
                .add(user.getId());
        log.info(
                "User({}) added like to Film({}).",
                user.getId(),
                film.getId()
        );
    }

    public void removeLike(
            Film film,
            User user
    ) {
        film.getLikes()
                .remove(user.getId());
        log.info(
                "User({}) removed like from Film({}).",
                user.getId(),
                film.getId()
        );
    }

    public List<Film> getTopFilms() {
        return getTopFilms(10);
    }

    public List<Film> getTopFilms(int size) {
        return filmStorage.findAll()
                .stream()
                .sorted(Comparator.comparingInt(filmA -> -filmA.getLikes().size()))
                .limit(size)
                .toList();
    }
}
