package ru.yandex.practicum.filmorate.storage.database;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.BaseModelRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

@Component
@Slf4j
@AllArgsConstructor
public class MpaDbStorage extends BaseDbStorage<Mpa> implements MpaStorage {
    private final MpaRepository repository;

    @Override
    protected BaseModelRepository<Mpa> getRepository() {
        return repository;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
