package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends AbstractModelsController<Film> {
    protected void validate(Film model) throws RuntimeException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String requiredMessage = "\"%s\" is required.";
        String maxLengthMessage = "Max length of \"%s\" is %d.";
        String minDateMessage = "Min \"%s\" is \"%s\".";
        String positiveNumberMessage = "The value of \"%s\" must be a positive number.";
        String invalidFormatMessage = "\"%s\" has invalid format.";
        int maxDescriptionLength = 200;

        // Name

        if (
                model.getName() == null
                        || model.getName().isEmpty()
        ) {
            throwValidationError(String.format(
                    requiredMessage, "name"
            ));
        }

        // Description

        if (model.getDescription() == null) {
            throwValidationError(String.format(
                    requiredMessage,
                    "description"
            ));
        }

        if (model.getDescription().length() > maxDescriptionLength) {
            throwValidationError(String.format(
                    maxLengthMessage,
                    "description",
                    maxDescriptionLength
            ));
        }

        // Release date

        Instant minReleaseDate;

        if (model.getReleaseDate() == null) {
            throwValidationError(String.format(
                    requiredMessage,
                    "releaseDate"
            ));
        }

        try {
            minReleaseDate = dateFormat.parse("1895-12-28").toInstant();
            if (dateFormat.parse(model.getReleaseDate()).toInstant().isBefore(minReleaseDate)) {
                throwValidationError(String.format(
                        minDateMessage,
                        "releaseDate",
                        minReleaseDate
                ));
            }
        } catch (ParseException exception) {
            throwValidationError(String.format(
                    invalidFormatMessage,
                    "releaseDate"
            ));
        }

        // Duration

        if (model.getDuration() == null) {
            throwValidationError(String.format(
                    requiredMessage,
                    "duration"
            ));
        }

        if (model.getDuration() <= 0) {
            throwValidationError(String.format(
                    positiveNumberMessage,
                    "duration"
            ));
        }
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
