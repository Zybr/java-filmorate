package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.http.BadRequestException;
import ru.yandex.practicum.filmorate.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends AbstractModelsController<User> {
    protected void validate(User user) throws RuntimeException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String requiredMessage = "\"%s\" is required.";
        String invalidFormatMessage = "\"%s\" has invalid format.";
        String maxDateMessage = "Max \"%s\" can be in the future";

        // Email

        if (user.getEmail().isEmpty()) {
            throw new BadRequestException(String.format(
                    requiredMessage,
                    "email"
            ));
        }

        if (!user.getEmail().contains("@")) {
            throw new BadRequestException(String.format(
                    invalidFormatMessage,
                    "email"
            ));
        }

        // Login

        if (user.getLogin().trim().isEmpty()) {
            throw new BadRequestException(String.format(
                    requiredMessage,
                    "login"
            ));
        }

        // Birthday

        Instant maxBirthday;

        try {
            maxBirthday = Instant.now();
            if (dateFormat.parse(user.getBirthday()).toInstant().isAfter(maxBirthday)) {
                throw new BadRequestException(String.format(
                        maxDateMessage,
                        "birthday"
                ));
            }
        } catch (ParseException exception) {
            throw new BadRequestException(String.format(
                    invalidFormatMessage,
                    "birthday"
            ));
        }
    }

    protected void fill(User user) {
        super.fill(user);
        user.setName(
                (user.getName() == null || user.getName().isEmpty())
                        ? user.getLogin()
                        : user.getName()
        );
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
