package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface FriendshipStorage {
    void addFriend(User userA, User userB);

    void removeFriend(User userA, User userB);

    boolean isFriend(User user, User friend);

    void assertJoinable(User userA, User userB) throws IllegalArgumentException;
}
