package ru.yandex.practicum.filmorate.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.database.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.database.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.memory.FilmMemoryStorage;
import ru.yandex.practicum.filmorate.storage.memory.LikeMemoryStorage;

@Configuration
public class FilmConfig {
    @Bean
    public FilmService filmMemoryService(
            FilmMemoryStorage filmStorage,
            @Qualifier("likeMemoryStorage")
            LikeMemoryStorage likeStorage
    ) {
        return new FilmService(
                filmStorage,
                likeStorage
        );
    }

    @Bean
    @Primary
    public FilmService filmDbService(
            FilmDbStorage filmStorage,
            LikeDbStorage likeStorage
    ) {
        return new FilmService(
                filmStorage,
                likeStorage
        );
    }

    @Bean
    public FilmMemoryStorage filmMemoryStorage() {
        return new FilmMemoryStorage();
    }

    @Bean
    @Primary
    public FilmDbStorage filmDbStorage(
            FilmRepository filmRepository,
            LikeRepository likeRepository,
            MpaRepository mpaRepository,
            CategoryRepository categoryRepository,
            GenreRepository genreRepository
    ) {
        return new FilmDbStorage(
                filmRepository,
                likeRepository,
                mpaRepository,
                categoryRepository,
                genreRepository
        );
    }
}
