package ru.yandex.practicum.filmorate.dal.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseModelMapper<M extends Model> implements RowMapper<M>, ParametersMapper<M> {
    private final ObjectMapper objectMapper;

    public BaseModelMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public HashMap<String, Object> mapModel(M model) {
        HashMap<String, Object> sqlData = new HashMap<>();
        HashMap<String, Object> data = objectMapper.convertValue(
                model,
                new TypeReference<>() {
                }
        );

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sqlData.put(
                    camelCaseToUnderscore(entry.getKey()),
                    entry.getValue()
            );
        }

        return sqlData;
    }

    public List<String> getChangeableValues(M model) {
        Map<String, Object> data = mapModel(model);

        return getChangeableFields()
                .stream()
                .map(data::get)
                .map(this::getSqlValue)
                .collect(Collectors.toList());
    }

    public String getSqlValue(Object value) {
        return value instanceof String
                ? String.format(
                "'%s'",
                ((String) value).replaceAll("'", "")
        )
                : String.valueOf(value);
    }

    public String getSqlValues(List<String> values) {
        return String.join(
                ", ",
                values
        );
    }

    public String getSqlWithValues(
            String sql,
            List<String> values
    ) {
        return String.format(
                sql,
                values.toArray()
        );
    }

    public List<String> getStoringFields() {
        return mapModel(getModelInstance())
                .keySet()
                .stream()
                .toList();
    }

    public List<String> getChangeableFields() {
        Set<String> changeableFields = new HashSet<>(getStoringFields());
        changeableFields.remove("id");

        return getStoringFields()
                .stream()
                .filter(changeableFields::contains)
                .collect(Collectors.toList());
    }

    public String getSqlUpdatingSet(M model) {
        List<String> changeable = getChangeableFields();

        return getSqlValues(
                mapModel(model)
                        .entrySet()
                        .stream()
                        .filter(entry -> changeable.contains(entry.getKey()))
                        .map(
                                (entry) ->
                                        String.format(
                                                "%s = %s",
                                                entry.getKey(),
                                                getSqlValue(
                                                        entry.getValue()
                                                )
                                        )
                        )
                        .toList()
        );
    }

    protected abstract M getModelInstance();

    protected Long getNullableLong(
            ResultSet resultSet,
            String columnName
    ) throws SQLException {
        long value = resultSet.getLong(columnName);
        return  value != 0L
                ? value
                : null;
    }

    private String camelCaseToUnderscore(String value) {
        return value
                .replaceAll("([A-Z])", "_$1")
                .toLowerCase();
    }
}
