package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dto.request.FilmSavingDto;
import ru.yandex.practicum.filmorate.exception.http.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.database.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final UserStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final FilmService filmService;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final FilmRowMapper filmRowMapper;

    @Autowired
    public FilmController(
            UserStorage userStorage,
            FilmDbStorage filmStorage,
            FilmService filmService,
            MpaStorage mpaStorage,
            GenreStorage genreStorage,
            FilmRowMapper filmRowMapper
    ) {
        super();
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.filmRowMapper = filmRowMapper;
    }

    @GetMapping("/{id}")
    public Film getOne(
            @PathVariable Long id
    ) {
        return filmStorage.findOrFail(id);
    }

    @GetMapping
    public List<Film> getAll() {
        return filmStorage.findAll();
    }

    @PostMapping
    public Film create(
            @RequestBody @Valid FilmSavingDto saving
    ) {
        validate(saving);
        return filmStorage.create(
                filmRowMapper.mapWithSaving(saving)
        );
    }

    @PutMapping
    public Film update(
            @RequestBody @Valid FilmSavingDto saving
    ) {
        validate(saving);

        return filmStorage.update(
                filmRowMapper.mapWithSaving(saving)
        );
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
            @RequestParam(defaultValue = "10") int count
    ) {
        return filmService.getTopFilms(count);
    }

    protected void validate(
            FilmSavingDto saving
    ) {
        this.validateReleaseDate(saving);
        this.validateMpa(saving);
        this.validateGenres(saving);
    }

    private void validateReleaseDate(
            FilmSavingDto saving
    ) {
        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28)
                .atStartOfDay()
                .toLocalDate();

        if (
                saving.getReleaseDate()
                        .isBefore(minReleaseDate)
        ) {
            String validationErrorMessage = "Validation failed: Invalid release date";
            log.warn(validationErrorMessage);
            throw new BadRequestException(validationErrorMessage);
        }
    }

    private void validateMpa(
            FilmSavingDto saving
    ) {
        if (saving.getMpa() != null) {
            this.mpaStorage.findOrFail(
                    saving
                            .getMpa()
                            .getId()
            );
        }
    }

    private void validateGenres(
            FilmSavingDto saving
    ) {
        if (saving.getGenres() != null) {
            saving
                    .getGenres()
                    .forEach(
                            modelDto -> genreStorage
                                    .findOrFail(modelDto.getId())
                    );
        }
    }
}
