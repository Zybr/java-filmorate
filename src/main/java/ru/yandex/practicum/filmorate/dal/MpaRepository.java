package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;


@Repository
public class MpaRepository extends BaseModelRepository<Mpa> {
    public MpaRepository(
            JdbcTemplate jdbc,
            MpaRowMapper mapper
    ) {
        super(
                jdbc,
                mapper
        );
    }

    @Override
    protected String getTableName() {
        return "mpas";
    }
}
