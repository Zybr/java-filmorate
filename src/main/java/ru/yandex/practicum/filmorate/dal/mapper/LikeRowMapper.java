package ru.yandex.practicum.filmorate.dal.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class LikeRowMapper extends BaseModelMapper<Like> {
    public LikeRowMapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Like mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Like.builder()
                .id(resultSet.getLong("id"))
                .userId(resultSet.getLong("user_id"))
                .filmId(resultSet.getLong("film_id"))
                .build();
    }

    @Override
    protected Like getModelInstance() {
        return Like
                .builder()
                .build();
    }
}
