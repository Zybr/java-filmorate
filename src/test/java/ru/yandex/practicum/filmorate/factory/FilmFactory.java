package ru.yandex.practicum.filmorate.factory;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.yandex.practicum.filmorate.dto.request.FilmSavingDto;
import ru.yandex.practicum.filmorate.dto.request.ModelDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.CategoryStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;


public class FilmFactory extends Factory<Film> {
    @Getter
    @Setter
    @Accessors(chain = true)
    private FilmStorage storage;

    @Getter
    @Setter
    @Accessors(chain = true)
    private MpaStorage mpaStorage;

    @Getter
    @Setter
    @Accessors(chain = true)
    private CategoryStorage categoryStorage;

    @Getter
    @Setter
    @Accessors(chain = true)
    private GenreStorage genreStorage;

    public Film make() {
        return Film
                .builder()
                .name(faker.company().name())
                .description(faker.lorem().sentence())
                .releaseDate(
                        dateToLocal(faker.date().birthday())
                )
                .duration(faker.number().numberBetween(30, 300))
                .build();
    }

    public FilmSavingDto makeSavingDto() {
        return FilmSavingDto
                .builder()
                .name(faker.company().name())
                .description(faker.lorem().sentence())
                .releaseDate(
                        dateToLocal(faker.date().birthday())
                )
                .duration(faker.number().numberBetween(30, 300))
                .mpa(
                        new ModelDto(
                                faker.options()
                                        .nextElement(mpaStorage.findAll())
                                        .getId()
                        )
                )
                .genres(List.of(
                        new ModelDto(
                                faker.options()
                                        .nextElement(genreStorage.findAll())
                                        .getId()
                        )
                ))
                .build();
    }
}
