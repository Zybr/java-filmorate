package ru.yandex.practicum.filmorate.factory;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;

public abstract class Factory<M> {
    protected final Faker faker = new Faker();

    public abstract M make();

    public List<M> makeList() {
        return makeList(10);
    }

    public List<M> makeList(int size) {
        List<M> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add(make());
        }

        return list;
    }

    protected Long makeId() {
        return Math.abs(faker.random().nextLong());
    }
}
