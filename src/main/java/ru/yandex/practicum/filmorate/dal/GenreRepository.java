package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;


@Repository
public class GenreRepository extends BaseModelRepository<Genre> {
    public GenreRepository(
            JdbcTemplate jdbc,
            GenreRowMapper mapper
    ) {
        super(
                jdbc,
                mapper
        );
    }

    @Override
    protected String getTableName() {
        return "genres";
    }
}
