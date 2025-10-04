package ru.yandex.practicum.filmorate.storage.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.BaseModelRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Component
@Slf4j
@AllArgsConstructor
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {
    private final GenreRepository repository;

    @Override
    protected BaseModelRepository<Genre> getRepository() {
        return repository;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
