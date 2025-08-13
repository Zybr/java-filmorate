package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.http.BadRequestException;
import ru.yandex.practicum.filmorate.exception.http.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private Long lastId = 0L;

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film creation) {
        validate(creation);
        fill(creation);
        save(creation);

        return creation;
    }

    @PutMapping
    public Film update(@RequestBody Film updating) {
        checkExistence(updating);
        validate(updating);
        fill(updating);
        save(updating);

        return updating;
    }

    private void checkExistence(Film film) throws NotFoundException {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException(String.format("There is no a Film with %d ID", film.getId()));
        }
    }

    private void validate(Film film) throws RuntimeException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String requiredMessage = "\"%s\" is required.";
        String maxLengthMessage = "Max length of \"%s\" is %d.";
        String minDateMessage = "Min \"%s\" is \"%s\".";
        String positiveNumberMessage = "The value of \"%s\" must be a positive number.";
        String invalidFormatMessage = "\"%s\" has invalid format.";

        int maxDescriptionLength = 200;

        // Name

        if (film.getName().isEmpty()) {
            throw new BadRequestException(String.format(
                    requiredMessage,
                    "name"
            ));
        }

        // Description

        if (film.getDescription().length() > maxDescriptionLength) {
            throw new BadRequestException(String.format(
                    maxLengthMessage,
                    "description",
                    200
            ));
        }

        // Release date

        Instant minReleaseDate;

        try {
            minReleaseDate = dateFormat.parse("1895-12-28").toInstant();
            if (dateFormat.parse(film.getReleaseDate()).toInstant().isBefore(minReleaseDate)) {
                throw new BadRequestException(String.format(
                        minDateMessage,
                        "releaseDate",
                        minReleaseDate
                ));
            }
        } catch (ParseException exception) {
            throw new BadRequestException(String.format(
                    invalidFormatMessage,
                    "releaseDate"
            ));
        }

        // Duration

        if (film.getDuration() <= 0) {
            throw new BadRequestException(String.format(
                    positiveNumberMessage,
                    "duration"
            ));
        }
    }

    private void fill(Film film) {
        film.setId(
                film.getId() != null
                        ? film.getId()
                        : ++lastId
        );
        this.films.put(film.getId(), film);
    }

    private void save(Film film) {
        this.films.put(film.getId(), film);
    }
}
