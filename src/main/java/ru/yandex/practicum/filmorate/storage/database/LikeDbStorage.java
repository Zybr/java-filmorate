package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.LikeRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.memory.LikeMemoryStorage;

@Component
@RequiredArgsConstructor
public class LikeDbStorage extends LikeMemoryStorage implements LikeStorage {
    private final LikeRepository likeRepository;

    @Override
    public void addLike(Film film, User user) {
        if (isLiked(film, user)) {
            return;
        }

        likeRepository.insertOne(
                Like.builder()
                        .filmId(film.getId())
                        .userId(user.getId())
                        .build()
        );

        super.addLike(film, user);
    }

    @Override
    public void removeLike(Film film, User user) {
        if (!isLiked(film, user)) {
            return;
        }

        likeRepository.deleteByFilmUserIds(
                film.getId(),
                user.getId()
        );
        super.removeLike(film, user);
    }

    private boolean isLiked(Film film, User user) {
        return film
                .getLikes()
                .contains(user.getId());
    }
}
