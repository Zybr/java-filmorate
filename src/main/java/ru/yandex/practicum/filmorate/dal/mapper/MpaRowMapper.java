package ru.yandex.practicum.filmorate.dal.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class MpaRowMapper extends BaseModelMapper<Mpa> {
    public MpaRowMapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Mpa mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    protected Mpa getModelInstance() {
        return Mpa
                .builder()
                .build();
    }
}
