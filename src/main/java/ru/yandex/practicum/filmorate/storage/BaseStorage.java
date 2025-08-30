package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import ru.yandex.practicum.filmorate.exception.storage.NotExistedModelException;
import ru.yandex.practicum.filmorate.model.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseStorage<M extends Model> implements Storage<M> {
    protected final Map<Long, M> models = new HashMap<>();
    protected Long lastId = 0L;

    public M findOne(Long id) {
        return models.getOrDefault(id, null);
    }

    public M findOrFail(Long id) {
        M model = this.findOne(id);

        if (model == null) {
            throw new NotExistedModelException(
                    String.format(
                            "There Model(%d)",
                            id
                    )
            );
        }

        return model;
    }

    public List<M> findAll() {
        return models
                .values()
                .stream()
                .toList();
    }

    public M create(M creation) {
        fill(creation);
        save(creation);

        return creation;
    }

    public M update(M updating) {
        if (!has(updating)) {
            throw new NotExistedModelException(String.format(
                    "There is no a Model(%d).",
                    updating.getId()
            ));
        }

        fill(updating);
        save(updating);

        return updating;
    }

    public boolean has(M model) {
        return models.containsKey(model.getId());
    }

    protected void fill(M model) {
        model.setId(
                model.getId() != null
                        ? model.getId()
                        : ++lastId
        );
    }

    protected void save(M model) {
        boolean isNew = !this.models
                .containsKey(
                        model.getId()
                );
        models.put(
                model.getId(),
                model
        );
        String modelName = model
                .getClass()
                .getSimpleName();

        getLogger().info(
                "{}({}) was {}.",
                modelName,
                model.getId(),
                isNew ? "created" : "updated"
        );
        getLogger().info(
                "There are {} {} models.",
                models.size(),
                modelName
        );
    }

    protected abstract Logger getLogger();
}
