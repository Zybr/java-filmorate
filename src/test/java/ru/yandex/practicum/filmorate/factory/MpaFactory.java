package ru.yandex.practicum.filmorate.factory;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Storage;


public class MpaFactory extends Factory<Mpa> {
    @Getter
    @Setter
    @Accessors(chain = true)
    private Storage<Mpa> storage;

    public Mpa make() {
        return Mpa
                .builder()
                .id(createId())
                .name(createUniqueWord())
                .build();
    }
}
