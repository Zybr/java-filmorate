package ru.yandex.practicum.filmorate.dal.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class GenreRowMapper extends BaseModelMapper<Genre> {
    public GenreRowMapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Genre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    protected Genre getModelInstance() {
        return Genre
                .builder()
                .build();
    }
}
