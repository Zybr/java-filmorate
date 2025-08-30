package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends ModelController<Film> {
    private final InMemoryUserStorage userStorage;
    private final InMemoryFilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(
            InMemoryUserStorage userStorage,
            InMemoryFilmStorage filmStorage,
            FilmService filmService
    ) {
        super();
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Object> addLike(
            @PathVariable Long id,
            @PathVariable Long userId
    ) {
        this.filmService.addLike(
                filmStorage.findOrFail(id),
                userStorage.findOrFail(userId)
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Object> removeLike(
            @PathVariable Long id,
            @PathVariable Long userId
    ) {
        this.filmService.removeLike(
                filmStorage.findOrFail(id),
                userStorage.findOrFail(userId)
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(
            @RequestParam(required = false, defaultValue = "10") int count
    ) {
        return filmService.getTopFilms(count);
    }

    protected void validate(Film model) throws RuntimeException {
        Date minReleaseDate = Date.from(
                LocalDate.of(1895, 12, 28)
                        .atStartOfDay()
                        .toInstant(ZoneOffset.UTC)
        );
        if (
                model.getReleaseDate().before(minReleaseDate)
        ) {
            throwValidationError("Invalid release date");
        }
    }

    @Override
    protected FilmStorage getStorage() {
        return this.filmStorage;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
