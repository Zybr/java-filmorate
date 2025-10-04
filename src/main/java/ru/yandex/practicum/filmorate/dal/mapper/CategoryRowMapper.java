package ru.yandex.practicum.filmorate.dal.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class CategoryRowMapper extends BaseModelMapper<Category> {
    public CategoryRowMapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Category mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Category.builder()
                .id(resultSet.getLong("id"))
                .filmId(resultSet.getLong("film_id"))
                .genreId(resultSet.getLong("genre_id"))
                .build();
    }

    @Override
    protected Category getModelInstance() {
        return Category
                .builder()
                .build();
    }
}
