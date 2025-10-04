package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Model;

import java.util.List;

public interface Storage<M extends Model> {
    M findOne(Long id);

    M findOrFail(Long id);

    List<M> findAll();

    int getSize();

    M create(M creation);

    M update(M updating);

    boolean has(M model);
}
