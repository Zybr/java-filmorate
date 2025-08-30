package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends ModelController<User> {
    private final InMemoryUserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(
            InMemoryUserStorage userStorage,
            UserService userService
    ) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getOne(
            @PathVariable Long id
    ) {
        return userStorage.findOrFail(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Object> addFriend(
            @PathVariable Long id,
            @PathVariable Long friendId
    ) {
        this.userService.joinFriends(
                userStorage.findOrFail(id),
                userStorage.findOrFail(friendId)
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Object> removeFriend(
            @PathVariable Long id,
            @PathVariable Long friendId
    ) {
        this.userService.splitFriends(
                userStorage.findOrFail(id),
                userStorage.findOrFail(friendId)
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(
            @PathVariable Long id
    ) {
        return this.userService
                .getFriends(
                        userStorage.findOrFail(id)
                );
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable Long id,
            @PathVariable Long otherId
    ) {
        return this.userService
                .getCommonFriends(
                        userStorage.findOrFail(id),
                        userStorage.findOrFail(otherId)
                );
    }

    @Override
    protected UserStorage getStorage() {
        return this.userStorage;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
