package ru.yandex.practicum.filmorate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.storage.database.MpaDbStorage;

@Configuration
public class MpaConfig {
    @Bean
    @Primary
    public MpaDbStorage mpaDbStorage(
            MpaRepository mpaRepository
    ) {
        return new MpaDbStorage(
                mpaRepository
        );
    }
}
