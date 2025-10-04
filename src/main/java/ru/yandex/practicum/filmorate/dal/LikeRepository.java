package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.LikeRowMapper;
import ru.yandex.practicum.filmorate.model.Like;


@Repository
public class LikeRepository extends BaseModelRepository<Like> {
    private static final String DELETE_BY_FILM_USER_IDS_QUERY = "DELETE FROM {{table}} " +
            "WHERE film_id = ?" +
            "AND user_id = ?";

    public LikeRepository(
            JdbcTemplate jdbc,
            LikeRowMapper mapper
    ) {
        super(
                jdbc,
                mapper
        );
    }

    public void deleteByFilmUserIds(
            long filmId,
            long userId
    ) {
        deleteIfExist(
                getSql(DELETE_BY_FILM_USER_IDS_QUERY),
                filmId,
                userId
        );
    }

    @Override
    protected String getTableName() {
        return "likes";
    }
}
