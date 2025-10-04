package ru.yandex.practicum.filmorate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.filmorate.dal.LikeRepository;
import ru.yandex.practicum.filmorate.storage.database.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.memory.LikeMemoryStorage;

@Configuration
public class LikeConfig {
    @Bean
    public LikeMemoryStorage likeMemoryStorage() {
        return new LikeMemoryStorage();
    }

    @Bean
    @Primary
    public LikeDbStorage likeDbStorage(
            LikeRepository likeRepository
    ) {
        return new LikeDbStorage(
                likeRepository
        );
    }
}
