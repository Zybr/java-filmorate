package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.BaseModelRepository;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
// TODO Fill "name" by "login"
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private final UserRepository repository;
    private final FriendshipRepository friendshipRepository;

    @Override
    protected BaseModelRepository<User> getRepository() {
        return repository;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    @Override
    public List<User> findAll() {
        Map<Long, Set<Long>> userFriends = getUserFriends();

        return super
                .findAll()
                .stream()
                .peek(user -> completeWithFriends(user, userFriends))
                .toList();
    }

    @Override
    public User findOrFail(Long id) {
        return completeWithFriends(
                super.findOrFail(id),
                getUserFriends()
        );
    }

    @Override
    public User findOne(Long id) {
        User user = super.findOne(id);

        if (user != null) {
            this.completeWithFriends(
                    user,
                    getUserFriends()
            );
        }

        return user;
    }

    private User completeWithFriends(
            User user,
            Map<Long, Set<Long>> userFriends
    ) {
        user.getFriends()
                .addAll(
                        userFriends
                                .getOrDefault(
                                        user.getId(),
                                        new HashSet<>()
                                )
                                .stream()
                                .toList()
                );

        return user;
    }

    private Map<Long, Set<Long>> getUserFriends() {
        return friendshipRepository
                .findAll()
                .stream()
                .reduce(
                        new HashMap<>(),
                        (userFriends, friendship) -> addRelation(
                                userFriends,
                                friendship.getUserId(),
                                friendship.getFriendId()
                        ),
                        (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        }
                );
    }

    @Override
    protected void fill(User user) {
        super.fill(user);
        user.setName(
                (user.getName() == null || user.getName().isEmpty())
                        ? user.getLogin()
                        : user.getName()
        );
    }

    private HashMap<Long, Set<Long>> addRelation(
            HashMap<Long, Set<Long>> userFriends,
            Long userIdA,
            Long userIdB
    ) {
        userFriends.put(
                userIdA,
                userFriends.getOrDefault(
                        userIdA,
                        new HashSet<>()
                )
        );
        userFriends
                .get(userIdA)
                .add(userIdB);

        return userFriends;
    }
}
