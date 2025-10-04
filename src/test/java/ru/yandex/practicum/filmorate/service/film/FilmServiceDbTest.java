package ru.yandex.practicum.filmorate.service.film;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

/**
 * @see FilmService
 */
class FilmServiceDbTest extends FilmServiceMemoryTest {
    @Autowired
    @Qualifier("filmDbStorage")
    @Getter
    private FilmStorage filmStorage;

    @Autowired
    @Qualifier("userDbStorage")
    @Getter
    private UserStorage userStorage;

    @Autowired
    @Qualifier("filmDbService")
    @Getter
    private FilmService service;
}
