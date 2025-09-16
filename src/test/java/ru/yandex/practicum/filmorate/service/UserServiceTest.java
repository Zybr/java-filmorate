package ru.yandex.practicum.filmorate.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.factory.UserFactory;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

/**
 * @see UserService
 */
class UserServiceTest {
    private final UserFactory userFactory = new UserFactory();
    private UserStorage userStorage;
    private UserService userService;

    @BeforeEach
    public void init() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
    }

    /**
     * @see UserService#joinFriends(User, User)
     */
    @Test()
    public void shouldJoinUsers() {
        User userA = userFactory.make();
        User userB = userFactory.make();

        userService.joinFriends(userA, userB);

        Assertions.assertTrue(
                userA.getFriends().contains(
                        userB.getId()
                )
        );
        Assertions.assertTrue(
                userB.getFriends().contains(
                        userA.getId()
                )
        );
    }

    /**
     * @see UserService#splitFriends(User, User)
     */
    @Test()
    public void shouldSplitUsers() {
        User userA = userFactory.make();
        User userB = userFactory.make();

        userService.joinFriends(userA, userB);
        userService.splitFriends(userA, userB);

        Assertions.assertFalse(
                userA.getFriends().contains(
                        userB.getId()
                )
        );
        Assertions.assertFalse(
                userB.getFriends().contains(
                        userA.getId()
                )
        );
    }

    /**
     * @see UserService#getFriends(User)
     */
    @Test
    public void shouldGetFriends() {
        User user = userStorage.create(
                userFactory.make()
        );

        for (int i = 0; i < 10; i++) {
            userStorage.create(userFactory.make());
        }

        List<Long> addedFriends = userStorage.findAll()
                .stream()
                .limit(3)
                .map(person -> {
                    user
                            .getFriends()
                            .add(person.getId());
                    return person.getId();
                })
                .toList();

        List<Long> userFriends = userService
                .getFriends(user)
                .stream()
                .map(User::getId)
                .toList();

        Assertions.assertEquals(
                addedFriends,
                userFriends
        );
    }

    /**
     * @see UserService#areFriends(User, User)
     */
    @Test()
    public void shouldCheckIfUsersAreFriends() {
        User userA = userFactory.make();
        User userB = userFactory.make();
        Assertions.assertFalse(
                userService.areFriends(
                        userA,
                        userB
                )
        );

        userService.joinFriends(userA, userB);
        Assertions.assertTrue(
                userService.areFriends(
                        userA,
                        userB
                )
        );

        userService.splitFriends(userA, userB);
        Assertions.assertFalse(
                userService.areFriends(
                        userA,
                        userB
                )
        );
    }

    /**
     * @see UserService#getCommonFriends(User, User)
     */
    @Test
    public void shouldGetCommonFriends() {
        User userA = userStorage.create(
                userFactory.make()
        );
        User userB = userStorage.create(
                userFactory.make()
        );

        List<User> users = userFactory.makeList(10)
                .stream()
                .map(userStorage::create)
                .toList();

        for (int i = 0; i <= 6; i++) {
            userService.joinFriends(userA, users.get(i));
        }

        for (int i = 5; i <= 9; i++) {
            userService.joinFriends(userB, users.get(i));
        }

        List<User> commonFriends = userService.getCommonFriends(userA, userB);

        /*
         * 0 1 2 3 4 5 6 7 8 9  - all users
         * 0 1 2 3 4 5 6        - user A friends
         *           5 6 7 8 9  - user B friends
         *           5 6        - common friends
         */
        Assertions.assertEquals(
                users
                        .stream()
                        .skip(5)
                        .limit(2)
                        .map(User::getId)
                        .sorted()
                        .toList(),
                commonFriends
                        .stream()
                        .map(User::getId)
                        .sorted()
                        .toList()
        );
    }
}
