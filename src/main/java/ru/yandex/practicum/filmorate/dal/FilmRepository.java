package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;


@Repository
public class FilmRepository extends BaseModelRepository<Film> {
    public FilmRepository(
            JdbcTemplate jdbc,
            FilmRowMapper mapper
    ) {
        super(
                jdbc,
                mapper
        );
    }

    @Override
    protected String getTableName() {
        return "films";
    }
}
