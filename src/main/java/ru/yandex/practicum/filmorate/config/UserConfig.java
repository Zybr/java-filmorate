package ru.yandex.practicum.filmorate.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.database.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.database.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.memory.FriendshipMemoryStorage;
import ru.yandex.practicum.filmorate.storage.memory.UserMemoryStorage;

@Configuration
public class UserConfig {
    @Bean
    public UserService userMemoryService(
            UserMemoryStorage userMemoryStorage,
            @Qualifier("friendshipMemoryStorage")
            FriendshipMemoryStorage friendshipMemoryStorage
    ) {
        return new UserService(
                userMemoryStorage,
                friendshipMemoryStorage
        );
    }

    @Bean
    @Primary
    public UserService userDbService(
            UserDbStorage userDbStorage,
            FriendshipDbStorage friendshipDbStorage
    ) {
        return new UserService(
                userDbStorage,
                friendshipDbStorage
        );
    }

    @Bean
    public UserMemoryStorage userMemoryStorage() {
        return new UserMemoryStorage();
    }

    @Bean
    @Primary
    public UserDbStorage userDbStorage(
            UserRepository userRepository,
            FriendshipRepository friendshipRepository
    ) {
        return new UserDbStorage(
                userRepository,
                friendshipRepository
        );
    }
}
