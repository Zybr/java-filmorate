package ru.yandex.practicum.filmorate.service.user;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@SpringBootTest()
class UserServiceDbTest extends UserServiceMemoryTest {
    @Autowired
    @Qualifier("userDbService")
    @Getter
    private UserService userService;

    @Autowired
    @Qualifier("userDbStorage")
    @Getter
    private UserStorage userStorage;
}
