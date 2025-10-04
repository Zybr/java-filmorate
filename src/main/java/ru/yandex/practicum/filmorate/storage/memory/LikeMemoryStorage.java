package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Component
@Slf4j
public class LikeMemoryStorage implements LikeStorage {
    @Override
    public void addLike(Film film, User user) {
        film.getLikes()
                .add(user.getId());
        log.info(
                "User(ID:{}) added like to Film({}).",
                user.getId(),
                film.getId()
        );
    }

    @Override
    public void removeLike(Film film, User user) {
        film.getLikes()
                .remove(user.getId());
        log.info(
                "User(ID:{}) removed like from Film({}).",
                user.getId(),
                film.getId()
        );
    }
}
