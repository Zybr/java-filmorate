package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.CategoryRowMapper;
import ru.yandex.practicum.filmorate.model.Category;


@Repository
public class CategoryRepository extends BaseModelRepository<Category> {
    private static final String DELETE_BY_FILM_GENRE_IDS_QUERY = "DELETE FROM {{table}} " +
            "WHERE film_id = ?" +
            "AND genre_id = ?";

    private static final String DELETE_BY_FILM_ID_QUERY = "DELETE FROM {{table}} " +
            "WHERE film_id = ?";

    public CategoryRepository(
            JdbcTemplate jdbc,
            CategoryRowMapper mapper
    ) {
        super(
                jdbc,
                mapper
        );
    }

    public void deleteByFilmGenreIds(
            long filmId,
            long genreId
    ) {
        deleteIfExist(
                getSql(DELETE_BY_FILM_GENRE_IDS_QUERY),
                filmId,
                genreId
        );
    }

    public void deleteByFilmId(
            long filmId
    ) {
        deleteIfExist(
                getSql(DELETE_BY_FILM_ID_QUERY),
                filmId
        );
    }


    @Override
    protected String getTableName() {
        return "categories";
    }
}
