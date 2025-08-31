package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@Component
public class InMemoryUserStorage extends BaseStorage<User> implements UserStorage {
    @Override
    protected void fill(User user) {
        super.fill(user);
        user.setName(
                (user.getName() == null || user.getName().isEmpty())
                        ? user.getLogin()
                        : user.getName()
        );
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
