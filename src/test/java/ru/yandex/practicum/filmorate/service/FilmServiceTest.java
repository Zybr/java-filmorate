package ru.yandex.practicum.filmorate.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.factory.FilmFactory;
import ru.yandex.practicum.filmorate.factory.UserFactory;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;

/**
 * @see FilmService
 */
class FilmServiceTest {
    private final FilmFactory filmFactory = new FilmFactory();
    private final UserFactory userFactory = new UserFactory();
    private FilmStorage filmStorage;
    private FilmService filmService;

    @BeforeEach
    public void initService() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
    }

    /**
     * @see FilmService#addLike(Film, User)
     */
    @Test()
    public void shouldAddLike() {
        Film film = filmFactory.make();
        User user = userFactory.make();

        filmService.addLike(film, user);
        Assertions.assertTrue(
                film.getLikes().contains(user.getId())
        );
        filmService.addLike(film, user);
        Assertions.assertTrue(
                film.getLikes().contains(user.getId())
        );
    }

    /**
     * @see FilmService#removeLike(Film, User)
     */
    @Test()
    public void shouldRemoveLike() {
        Film film = filmFactory.make();
        User user = userFactory.make();

        filmService.addLike(film, user);
        filmService.removeLike(film, user);
        Assertions.assertFalse(
                film.getLikes().contains(user.getId())
        );
        filmService.removeLike(film, user);
        Assertions.assertFalse(
                film.getLikes().contains(user.getId())
        );
    }

    /**
     * @see FilmService#getTopFilms()
     */
    @Test
    public void shouldGetTopFilms() {
        List<User> users = userFactory.makeList();

        for (int i = 0; i < 15; i++) {
            Film film = filmStorage.create(filmFactory.make());

            for (int j = 0; j < 8; j++) {
                User user = users.get((int) Math.floor(Math.random() * users.size()));
                filmService.addLike(film, user);
            }
        }

        int prevRate = Integer.MAX_VALUE;
        List<Film> topFilms = filmService.getTopFilms();

        Assertions.assertEquals(10, topFilms.size());

        for (Film topFilm : topFilms) {
            int currRate = topFilm.getLikes().size();
            Assertions.assertTrue(prevRate >= currRate);
            prevRate = currRate;
        }
    }
}
