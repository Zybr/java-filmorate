package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.http.BadRequestException;
import ru.yandex.practicum.filmorate.model.Model;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public abstract class ModelController<M extends Model> {
    @GetMapping
    public List<M> getAll() {
        return getStorage().findAll();
    }

    @PostMapping
    public M create(@RequestBody @Valid M creation) {
        validate(creation);
        return getStorage().create(creation);
    }

    @PutMapping
    public M update(@RequestBody @Valid M updating) {
        validate(updating);
        return getStorage().update(updating);
    }

    protected void throwValidationError(String errorMessage) {
        String validationErrorMessage = "Validation failed: " + errorMessage;
        getLogger().warn(validationErrorMessage);
        throw new BadRequestException(validationErrorMessage);
    }

    protected void validate(M model) throws RuntimeException {
        // Override for adding extra validation
    }

    protected abstract Logger getLogger();

    protected abstract Storage<M> getStorage();
}
