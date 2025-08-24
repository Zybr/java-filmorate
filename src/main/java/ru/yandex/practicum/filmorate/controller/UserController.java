package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends AbstractModelsController<User> {
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
