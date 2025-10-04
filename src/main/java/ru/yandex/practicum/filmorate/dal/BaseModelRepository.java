package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.mapper.BaseModelMapper;
import ru.yandex.practicum.filmorate.model.Model;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                                        mapper.getSqlKeys(
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

    public M insertOne(M model) {
        model.setId(
                insertOne(
                        mapper.getSqlWithValues(
                                getSql(INSERT_ONE_QUERY),
                                List.of(
                                        mapper.getSqlKeys(mapper.getChangeableFields()),
                                        mapper.getSqlValues(mapper.getChangeableValues(model))
                                )
                        )
                )
        );

        return model;
    }

    public void insertMany(List<M> models) throws SQLException {
        if (models.isEmpty()) {
            return;
        }

        List<String> fields = mapper
                .getChangeableFields()
                .stream()
                .map(Object::toString)
                .toList();

        String insertSql = String.format(
                getSql(INSERT_ONE_QUERY),
                String.join(", ", fields),
                fields.stream().map(f -> "?")
                        .collect(Collectors.joining(", "))
        );

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(insertSql)
        ) {
            connection.setAutoCommit(false);

            for (M model : models) {
                List<Object> values = mapper.getChangeableValues(model);

                for (int i = 0; i < values.size(); i++) {
                    statement.setObject(i + 1, values.get(i));
                }

                statement.addBatch();
            }

            try {
                statement.executeBatch();
                connection.commit();
            } catch (BatchUpdateException batchUpdateException) {
                connection.rollback();
                throw batchUpdateException;
            }

        }
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
