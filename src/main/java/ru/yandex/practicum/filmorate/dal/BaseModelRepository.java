package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.mapper.BaseModelMapper;
import ru.yandex.practicum.filmorate.model.Model;

import java.util.List;
import java.util.Optional;

public abstract class BaseModelRepository<M extends Model> extends BaseRepository<M> {
    private static final String FIND_ALL_QUERY = "SELECT %s FROM {{table}}";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM {{table}} " +
            "WHERE id = ?";
    private static final String GET_TOTAL_COUNT = "SELECT COUNT(*) FROM {{table}}";
    private static final String INSERT_ONE_QUERY = "INSERT INTO {{table}}(%s) VALUES (%s)";
    private static final String UPDATE_ONE_QUERY = "UPDATE {{table}} SET " +
            "%s" +
            "WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE {{table}}" +
            "WHERE id = ?";

    public BaseModelRepository(
            JdbcTemplate jdbc,
            BaseModelMapper<M> mapper
    ) {
        super(
                jdbc,
                mapper
        );
    }

    public Optional<M> findById(long id) {
        return selectOne(
                getSql(FIND_BY_ID_QUERY),
                id
        );
    }

    public boolean hasWithId(long id) {
        return findById(id).isPresent();
    }

    public List<M> findAll() {
        return selectMany(
                getSql(
                        mapper.getSqlWithValues(
                                FIND_ALL_QUERY,
                                List.of(
                                        mapper.getSqlValues(
                                                mapper.getStoringFields()
                                        )
                                )
                        )
                )
        );
    }

    public int getCount() {
        return selectCount(
                getSql(GET_TOTAL_COUNT)
        );
    }

    public M insert(M model) {
        model.setId(
                insertOne(
                        mapper.getSqlWithValues(
                                getSql(INSERT_ONE_QUERY),
                                List.of(
                                        mapper.getSqlValues(mapper.getChangeableFields()),
                                        mapper.getSqlValues(mapper.getChangeableValues(model))
                                )
                        )
                )
        );

        return model;
    }

    public M update(M model) {
        updateOne(
                mapper.getSqlWithValues(
                        getSql(UPDATE_ONE_QUERY),
                        List.of(mapper.getSqlUpdatingSet(model))
                ),
                model.getId()
        );

        return model;
    }

    public void delete(M model) {
        super.deleteIfExist(
                getSql(DELETE_QUERY),
                model.getId()
        );
    }

    protected abstract String getTableName();

    protected String getSql(String sql) {
        return sql.replace("{{table}}", getTableName());
    }
}
