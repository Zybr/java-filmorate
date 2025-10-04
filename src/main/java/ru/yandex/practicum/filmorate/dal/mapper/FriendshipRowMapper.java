package ru.yandex.practicum.filmorate.dal.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class FriendshipRowMapper extends BaseModelMapper<Friendship> {
    public FriendshipRowMapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Friendship mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Friendship.builder()
                .id(resultSet.getLong("id"))
                .userId(resultSet.getLong("user_id"))
                .friendId(resultSet.getLong("friend_id"))
                .build();
    }

    @Override
    protected Friendship getModelInstance() {
        return Friendship
                .builder()
                .build();
    }
}
