package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.http.BadRequestException;
import ru.yandex.practicum.filmorate.exception.http.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private Long lastId = 0L;

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User creation) {
        validate(creation);
        fill(creation);
        save(creation);

        return creation;
    }

    @PutMapping
    public User update(@RequestBody User updating) {
        checkExistence(updating);
        validate(updating);
        fill(updating);
        save(updating);

        return updating;
    }

    private void checkExistence(User user) throws NotFoundException {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException(String.format("There is no a User with %d ID", user.getId()));
        }
    }

    private void validate(User user) throws RuntimeException {
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

    private void fill(User user) {
        user.setId(
                user.getId() != null
                        ? user.getId()
                        : ++lastId
        );
        user.setName(
                (user.getName() == null || user.getName().isEmpty())
                        ? user.getLogin()
                        : user.getName()
        );
        this.users.put(user.getId(), user);
    }

    private void save(User user) {
        this.users.put(user.getId(), user);
    }
}
