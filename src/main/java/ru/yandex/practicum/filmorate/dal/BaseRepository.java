package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.mapper.BaseModelMapper;
import ru.yandex.practicum.filmorate.exception.storage.NotExistedModelException;
import ru.yandex.practicum.filmorate.model.Model;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseRepository<T extends Model> {
    protected final JdbcTemplate jdbc;
    protected final BaseModelMapper<T> mapper;

    protected T selectOneOrFail(
            String sql,
            Object... params
    ) {
        return selectOne(sql, params)
                .orElseThrow(() -> new RuntimeException("There is no row."));
    }

    protected Optional<T> selectOne(
            String sql,
            Object... params
    ) {
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject(
                            sql,
                            mapper,
                            params
                    )
            );
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    protected List<T> selectMany(
            String sql,
            Object... params
    ) {
        return jdbc.query(
                sql,
                mapper,
                params
        );
    }

    protected int selectCount(
            String sql,
            Object... params
    ) {
        return jdbc.queryForObject(
                sql,
                Integer.class,
                params
        );
    }

    protected Long insertOne(String sql) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            int changesCount = statement.executeUpdate();

            if (changesCount == 0) {
                throw new RuntimeException("No rows inserted.");
            }

            if (changesCount > 1) {
                throw new RuntimeException("Several rows were changed.");
            }

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet
                            .getLong(1);
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void updateOne(
            String sql,
            Object... params
    ) {
        int changesCount = change(sql, params);

        if (changesCount == 0) {
            throw new NotExistedModelException("There is no a Model");
        }

        if (changesCount > 1) {
            throw new RuntimeException("More than 1 rows were updated.");
        }
    }

    protected void deleteIfExist(
            String sql,
            Object... params
    ) {
        change(sql, params);
    }

    protected Connection getConnection() throws SQLException {
        DataSource dataSource = jdbc.getDataSource();

        if (dataSource == null) {
            throw new RuntimeException("There is no database connection.");
        }

        return dataSource.getConnection();
    }

    private int change(
            String sql,
            Object... params
    ) {
        return jdbc.update(sql, params);
    }
}
