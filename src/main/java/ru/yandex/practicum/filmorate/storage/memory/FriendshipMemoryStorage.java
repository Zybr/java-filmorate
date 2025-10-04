package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

@Component
@Slf4j
public class FriendshipMemoryStorage implements FriendshipStorage {
    public void addFriend(
            User user,
            User friend
    ) {
        assertJoinable(user, friend);
        user.getFriends()
                .add(friend.getId());
        log.info(
                "User(ID:{}) was connected to friend User(ID:{})",
                user.getId(),
                friend.getId()
        );
    }

    public void removeFriend(
            User user,
            User friend
    ) {
        assertJoinable(user, friend);
        user.getFriends()
                .remove(friend.getId());
        log.info(
                "User(ID:{}) was disconnected from friend User(ID:{})",
                user.getId(),
                friend.getId()
        );
    }

    @Override
    public boolean isFriend(User user, User friend) {
        return user
                .getFriends()
                .contains(friend.getId());
    }

    public void assertJoinable(
            User userA,
            User userB
    ) throws IllegalArgumentException {
        if (userA.equals(userB)) {
            throw new IllegalArgumentException("Friends can't be the same User");
        }
    }
}
