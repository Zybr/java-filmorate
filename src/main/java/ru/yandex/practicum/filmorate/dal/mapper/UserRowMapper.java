package ru.yandex.practicum.filmorate.dal.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Component
public class UserRowMapper extends BaseModelMapper<User> {
    public UserRowMapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    @Override
    public List<String> getStoringFields() {
        return super
                .getStoringFields()
                .stream()
                .filter(field -> !field.equals("friends"))
                .toList();
    }

    @Override
    protected User getModelInstance() {
        return User
                .builder()
                .build();
    }
}
