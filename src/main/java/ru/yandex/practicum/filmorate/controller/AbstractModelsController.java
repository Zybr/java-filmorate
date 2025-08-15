package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.http.BadRequestException;
import ru.yandex.practicum.filmorate.exception.http.NotFoundException;
import ru.yandex.practicum.filmorate.model.AbstractModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractModelsController<T extends AbstractModel> {
    protected final Map<Long, T> models = new HashMap<>();

    protected Long lastId = 0L;

    @GetMapping
    public Collection<T> getAll() {
        return models.values();
    }

    @PostMapping
    public T create(@RequestBody T creation) {
        validate(creation);
        fill(creation);
        save(creation);

        return creation;
    }

    @PutMapping
    public T update(@RequestBody T updating) {
        checkExistence(updating);
        validate(updating);
        fill(updating);
        save(updating);

        return updating;
    }

    private void checkExistence(T model) throws NotFoundException {
        if (!models.containsKey(model.getId())) {
            throw new NotFoundException(String.format("There is no a model with %d ID", model.getId()));
        }
    }

    protected void fill(T model) {
        model.setId(
                model.getId() != null
                        ? model.getId()
                        : ++lastId
        );
    }

    protected void throwValidationError(String errorMessage) throws BadRequestException {
        String validationErrorMessage = "Validation failed: " + errorMessage;
        getLogger().warn(validationErrorMessage);
        throw new BadRequestException(validationErrorMessage);
    }

    protected void save(T model) {
        getLogger().info(
                "Model with {} ID was {}.",
                model.getId(),
                this.models.containsKey(model.getId()) ? "updated" : "created"
        );

        this.models.put(model.getId(), model);
    }

    protected abstract void validate(T model) throws RuntimeException;

    protected abstract Logger getLogger();
}
