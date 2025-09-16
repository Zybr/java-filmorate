package ru.yandex.practicum.filmorate.factory;


import com.github.javafaker.Faker;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.util.Date;


public class UserFactory extends Factory<User> {
    private final Faker faker = new Faker();

    public User make() {
        return User
                .builder()
                .email(faker.internet().emailAddress())
                .login(faker.lorem().word())
                .name(faker.company().name())
                .birthday(Date.from(Instant.now()))
                .build();
    }
}
