package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Slf4j
@Component
public class FilmMemoryStorage extends BaseMemoryStorage<Film> implements FilmStorage {
    @Override
    protected Logger getLogger() {
        return log;
    }
}
