package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    public FilmService(
            FilmStorage filmStorage,
            LikeStorage likeStorage
    ) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
    }

    public void addLike(
            Film film,
            User user
    ) {
        likeStorage.addLike(film, user);
    }

    public void removeLike(
            Film film,
            User user
    ) {
        likeStorage.removeLike(film, user);
    }

    public List<Film> getTopFilms() {
        return getTopFilms(10);
    }

    public List<Film> getTopFilms(int size) {
        return filmStorage
                .findAll()
                .stream()
                .sorted(Comparator.comparingInt(filmA -> -filmA.getLikes().size()))
                .limit(size)
                .toList();
    }
}
