package ru.yandex.practicum.filmorate.dal.mapper;

import ru.yandex.practicum.filmorate.model.Model;

import java.util.List;
import java.util.Map;

public interface ParametersMapper<M extends Model> {
    Map<String, Object> mapModel(M model);

    List<String> getChangeableFields();

    List<Object> getChangeableValues(M model);

    List<String> getStoringFields();

    String getSqlValue(Object value);

    String getSqlValues(List<Object> values);

    String getSqlKeys(List<String> values);
}
