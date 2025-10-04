package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    public UserService(
            UserStorage userStorage,
            FriendshipStorage friendshipStorage
    ) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public void addFriend(
            User user,
            User friend
    ) {
        friendshipStorage.addFriend(
                user,
                friend
        );
    }

    public void removeFriend(
            User user,
            User friend
    ) {
        friendshipStorage.removeFriend(
                user,
                friend
        );
    }

    public List<User> getFriends(
            User user
    ) {
        return user.getFriends()
                .stream()
                .map(userStorage::findOne)
                .toList();
    }

    public boolean isFriend(
            User user,
            User friend
    ) {
        return friendshipStorage
                .isFriend(user, friend);
    }

    public List<User> getCommonFriends(
            User userA,
            User userB
    ) {
        friendshipStorage.assertJoinable(userA, userB);
        Set<Long> intersection = new HashSet<>(userA.getFriends());
        intersection.retainAll(userB.getFriends());

        return intersection
                .stream()
                .map(userStorage::findOne)
                .toList();
    }
}
