package ru.yandex.practicum.filmorate.service.user;


import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.factory.UserFactory;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

/**
 * @see UserService
 */
@SpringBootTest()
class UserServiceMemoryTest {
    @Getter
    private final UserFactory userFactory = new UserFactory();

    @Autowired
    @Qualifier("userMemoryService")
    @Getter
    private UserService userService;

    @Autowired
    @Qualifier("userMemoryStorage")
    @Getter
    private UserStorage userStorage;

    @BeforeEach
    public void initFactories() {
        getUserFactory()
                .setStorage(
                        getUserStorage()
                );
    }

    /**
     * @see UserService#addFriend(User, User)
     */
    @Test
    public void shouldAddUserFriend() {
        User user = getUserFactory().create();
        User friend = getUserFactory().create();

        getUserService().addFriend(user, friend);

        Assertions.assertTrue(
                user.getFriends().contains(
                        friend.getId()
                )
        );
        Assertions.assertFalse(
                friend.getFriends().contains(
                        user.getId()
                )
        );
    }

    /**
     * @see UserService#removeFriend(User, User)
     */
    @Test
    public void shouldRemoveFriend() {
        User user = getUserFactory().create();
        User friend = getUserFactory().create();

        // User -> Friend: Add
        getUserService().addFriend(user, friend);
        // User -> Friend: Remove
        getUserService().removeFriend(user, friend);

        // User -> Friend: -
        Assertions.assertFalse(
                user.getFriends().contains(
                        friend.getId()
                )
        );
        // User <- Friend: -
        Assertions.assertFalse(
                friend.getFriends().contains(
                        user.getId()
                )
        );
    }

    /**
     * @see UserService#getFriends(User)
     */
    @Test
    public void shouldGetUserFriends() {
        User user = getUserFactory().create();

        List<Long> addedFriends = getUserFactory()
                .createList(10)
                .stream()
                .limit(3)
                .map(person -> {
                            getUserService()
                                    .addFriend(user, person);
                            return person.getId();
                        }
                )
                .toList();

        List<Long> userFriends = getUserService()
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
     * @see UserService#isFriend(User, User)
     */
    @Test
    public void shouldCheckIfUserIsFriend() {
        User user = getUserFactory().create();
        User friend = getUserFactory().create();
        Assertions.assertFalse(
                getUserService().isFriend(
                        user,
                        friend
                )
        );

        // User -> Friend: Add

        getUserService().addFriend(user, friend);
        // User -> Friend: +
        Assertions.assertTrue(
                getUserService().isFriend(
                        user,
                        friend
                )
        );
        // User <- Friend: -
        Assertions.assertFalse(
                getUserService().isFriend(
                        friend,
                        user
                )
        );

        // User -> Friend: Remove

        getUserService().removeFriend(user, friend);
        // User -> Friend: -
        Assertions.assertFalse(
                getUserService().isFriend(
                        user,
                        friend
                )
        );
        // User <- Friend: -
        Assertions.assertFalse(
                getUserService().isFriend(
                        friend,
                        user
                )
        );
    }

    /**
     * @see UserService#getCommonFriends(User, User)
     */
    @Test
    public void shouldGetCommonFriends() {
        User userA = getUserFactory().create();
        User userB = getUserFactory().create();
        List<User> users = getUserFactory().createList();

        for (int i = 0; i <= 6; i++) {
            getUserService().addFriend(userA, users.get(i));
        }

        for (int i = 5; i <= 9; i++) {
            getUserService().addFriend(userB, users.get(i));
        }

        List<User> commonFriends = getUserService().getCommonFriends(userA, userB);

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
