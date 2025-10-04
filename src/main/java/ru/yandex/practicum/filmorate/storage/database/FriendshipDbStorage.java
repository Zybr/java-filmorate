package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.memory.FriendshipMemoryStorage;

@Component
@RequiredArgsConstructor
public class FriendshipDbStorage extends FriendshipMemoryStorage {
    private final FriendshipRepository friendshipRepository;

    public void addFriend(
            User user,
            User friend
    ) {
        if (isFriend(
                user,
                friend
        )) {
            return;
        }

        friendshipRepository.insertOne(
                Friendship.builder()
                        .userId(user.getId())
                        .friendId(friend.getId())
                        .build()
        );

        super.addFriend(
                user,
                friend
        );
    }

    public void removeFriend(
            User user,
            User friend
    ) {
        if (!isFriend(user, friend)) {
            return;
        }

        friendshipRepository.deleteByUserIds(
                user.getId(),
                friend.getId()
        );

        super.removeFriend(
                user,
                friend
        );
    }
}
