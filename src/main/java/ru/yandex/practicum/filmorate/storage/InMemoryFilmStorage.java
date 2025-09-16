package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import org.slf4j.Logger;

@Slf4j
@Component
public class InMemoryFilmStorage extends BaseStorage<Film> implements FilmStorage {
    @Override
    protected Logger getLogger() {
        return log;
    }
}
