package ru.yandex.practicum.filmorate.service.film;


import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.factory.FilmFactory;
import ru.yandex.practicum.filmorate.factory.UserFactory;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.BaseServiceTest;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

/**
 * @see FilmService
 */
@SpringBootTest()
class FilmServiceMemoryTest extends BaseServiceTest<FilmService> {
    @Getter
    private final UserFactory userFactory = new UserFactory();

    @Getter
    private final FilmFactory filmFactory = new FilmFactory();

    @Autowired
    @Qualifier("filmMemoryStorage")
    @Getter
    private FilmStorage filmStorage;

    @Autowired
    @Qualifier("userMemoryStorage")
    @Getter
    private UserStorage userStorage;

    @Autowired
    @Qualifier("filmMemoryService")
    @Getter
    private FilmService service;

    @BeforeEach
    public void initFactories() {
        getUserFactory()
                .setStorage(
                        getUserStorage()
                );
        getFilmFactory()
                .setStorage(
                        getFilmStorage()
                );
    }

    /**
     * @see FilmService#addLike(Film, User)
     */
    @Test
    public void shouldAddLike() {
        Film film = getFilmFactory().create();
        User user = getUserFactory().create();

        getService().addLike(film, user);
        Assertions.assertTrue(
                film.getLikes().contains(user.getId())
        );
        getService().addLike(film, user);
        Assertions.assertTrue(
                film.getLikes().contains(user.getId())
        );
    }

    /**
     * @see FilmService#removeLike(Film, User)
     */
    @Test
    public void shouldRemoveLike() {
        Film film = getFilmFactory().create();
        User user = getUserFactory().create();

        getService().addLike(film, user);
        getService().removeLike(film, user);
        Assertions.assertFalse(
                film.getLikes()
                        .contains(user.getId())
        );
        getService().removeLike(film, user);
        Assertions.assertFalse(
                film.getLikes()
                        .contains(user.getId())
        );
    }

    /**
     * @see FilmService#getTopFilms()
     */
    @Test
    public void shouldGetTopFilms() {
        List<User> users = getUserFactory().createList();

        for (int i = 0; i < 15; i++) {
            Film film = getFilmFactory().create();

            for (int j = 0; j < 8; j++) {
                User user = users.get((int) Math.floor(Math.random() * users.size()));
                getService().addLike(film, user);
            }
        }

        int prevRate = Integer.MAX_VALUE;
        List<Film> topFilms = getService().getTopFilms();

        Assertions.assertEquals(10, topFilms.size());

        for (Film topFilm : topFilms) {
            int currRate = topFilm.getLikes().size();
            Assertions.assertTrue(prevRate >= currRate);
            prevRate = currRate;
        }
    }
}
