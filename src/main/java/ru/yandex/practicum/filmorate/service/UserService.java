package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void joinFriends(
            User userA,
            User userB
    ) {
        checkFriendshipRelation(userA, userB);
        userA.getFriends()
                .add(userB.getId());
        userB.getFriends()
                .add(userA.getId());
        log.info(
                "User({}) and User({}) where connected.",
                userA.getId(),
                userB.getId()
        );
    }

    public void splitFriends(
            User userA,
            User userB
    ) {
        checkFriendshipRelation(userA, userB);
        userA.getFriends()
                .remove(userB.getId());
        userB.getFriends()
                .remove(userA.getId());
        log.info(
                "User({}) and User({}) where disconnected.",
                userA.getId(),
                userB.getId()
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

    public boolean areFriends(
            User userA,
            User userB
    ) {
        return userA.getFriends()
                .contains(userB.getId());
    }

    public List<User> getCommonFriends(
            User userA,
            User userB
    ) {
        checkFriendshipRelation(userA, userB);
        Set<Long> intersection = new HashSet<>(userA.getFriends());
        intersection.retainAll(userB.getFriends());

        return intersection
                .stream()
                .map(userStorage::findOne)
                .toList();
    }

    private void checkFriendshipRelation(
            User userA,
            User userB
    ) {
        if (userA.equals(userB)) {
            throw new IllegalArgumentException("Friends can't be the same User");
        }
    }
}
