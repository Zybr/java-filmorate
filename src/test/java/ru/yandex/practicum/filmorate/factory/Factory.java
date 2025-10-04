package ru.yandex.practicum.filmorate.factory;

import com.github.javafaker.Faker;
import ru.yandex.practicum.filmorate.model.Model;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Factory<M extends Model> {
    protected final Faker faker = new Faker();

    public abstract M make();

    public M create() {
        return getStorage()
                .create(make());
    }

    public List<M> makeList() {
        return makeList(10);
    }

    public List<M> makeList(int size) {
        return collectList(size, this::make);
    }

    public List<M> createList() {
        return createList(10);
    }

    public List<M> createList(int size) {
        return collectList(size, this::create);
    }

    private List<M> collectList(int size, Create<M> create) {
        List<M> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add(create());
        }

        return list;
    }

    protected Long createId() {
        return Math.abs(faker.random().nextLong(1000));
    }

    protected String createUniqueWord() {
        return faker.lorem().word() + createId();
    }

    protected abstract Storage<M> getStorage();

    protected LocalDate dateToLocal(
            Date date
    ) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    @FunctionalInterface
    private interface Create<M> {
        M create();
    }
}
