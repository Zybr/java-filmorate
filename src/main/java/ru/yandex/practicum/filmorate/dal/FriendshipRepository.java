package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.FriendshipRowMapper;
import ru.yandex.practicum.filmorate.model.Friendship;


@Repository
public class FriendshipRepository extends BaseModelRepository<Friendship> {
    private static final String DELETE_BY_USER_IDS_QUERY = "DELETE FROM friendships"
            + " WHERE (user_id = ? AND friend_id = ?)";

    public FriendshipRepository(
            JdbcTemplate jdbc,
            FriendshipRowMapper mapper
    ) {
        super(
                jdbc,
                mapper
        );
    }

    public void deleteByUserIds(
            long userId,
            long friendId
    ) {
        deleteIfExist(
                getSql(DELETE_BY_USER_IDS_QUERY),
                userId,
                friendId
        );
    }

    @Override
    protected String getTableName() {
        return "friendships";
    }
}
