package ru.yandex.practicum.filmorate.storage.database;

import org.slf4j.Logger;
import ru.yandex.practicum.filmorate.dal.BaseModelRepository;
import ru.yandex.practicum.filmorate.exception.storage.NotExistedModelException;
import ru.yandex.practicum.filmorate.model.Model;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public abstract class BaseDbStorage<M extends Model> implements Storage<M> {
    public M findOne(Long id) {
        return this.getRepository()
                .findById(id)
                .orElse(null);
    }

    public M findOrFail(Long id) {
        return this.getRepository()
                .findById(id)
                .orElseThrow(
                        () -> new NotExistedModelException(
                                String.format(
                                        "There is no Model(%d).",
                                        id
                                )
                        )
                );
    }

    public List<M> findAll() {
        return this.getRepository()
                .findAll();
    }

    @Override
    public int getSize() {
        return this.getRepository()
                .getCount();
    }

    public M create(M creation) {
        fill(creation);
        M model = this.getRepository()
                .insert(creation);

        String modelName = model
                .getClass()
                .getSimpleName();

        getLogger().info(
                "{}(ID:{}) was created.",
                modelName,
                model.getId()
        );

        return model;
    }

    public M update(M updating) {
        fill(updating);

        return this.getRepository()
                .update(updating);
    }

    public boolean has(M model) {
        return this.getRepository()
                .hasWithId(model.getId());
    }

    protected abstract BaseModelRepository<M> getRepository();

    protected abstract Logger getLogger();

    protected void fill(M model) {
        // Update model before saving
    }
}
