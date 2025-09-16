package ru.yandex.practicum.filmorate.factory;


import ru.yandex.practicum.filmorate.model.Film;


public class FilmFactory extends Factory<Film> {

    public Film make() {
        return Film
                .builder()
                .id(makeId())
                .name(faker.company().name())
                .description(faker.lorem().sentence())
                .releaseDate(faker.date().birthday())
                .duration(faker.number().numberBetween(30, 300))
                .build();
    }
}
