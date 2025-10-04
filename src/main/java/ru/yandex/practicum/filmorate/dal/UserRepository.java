package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;


@Repository
public class UserRepository extends BaseModelRepository<User> {
    public UserRepository(
            JdbcTemplate jdbc,
            UserRowMapper mapper
    ) {
        super(
                jdbc,
                mapper
        );
    }

    @Override
    protected String getTableName() {
        return "users";
    }
}
