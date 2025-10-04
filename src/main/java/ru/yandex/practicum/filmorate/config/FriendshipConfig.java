package ru.yandex.practicum.filmorate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.storage.database.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.memory.FriendshipMemoryStorage;

@Configuration
public class FriendshipConfig {
    @Bean
    public FriendshipMemoryStorage friendshipMemoryService(
    ) {
        return new FriendshipMemoryStorage();
    }

    @Bean
    @Primary
    public FriendshipDbStorage friendshipDbService(
            FriendshipRepository friendshipRepository
    ) {
        return new FriendshipDbStorage(
                friendshipRepository
        );
    }
}
