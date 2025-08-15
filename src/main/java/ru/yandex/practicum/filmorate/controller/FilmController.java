package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends AbstractModelsController<Film> {
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
    protected Logger getLogger() {
        return log;
    }
}
