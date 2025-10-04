package ru.yandex.practicum.filmorate.factory;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;


public class UserFactory extends Factory<User> {
    @Getter
    @Setter
    @Accessors(chain = true)
    private Storage<User> storage;

    public User make() {
        return User
                .builder()
                .email(createUniqueWord() + faker.internet().emailAddress())
                .login(createUniqueWord())
                .name(createUniqueWord())
                .birthday(LocalDate.now().minusYears(5))
                .build();
    }
}
