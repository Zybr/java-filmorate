package ru.yandex.practicum.filmorate.dal.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.request.FilmSavingDto;
import ru.yandex.practicum.filmorate.dto.request.ModelDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
public class FilmRowMapper extends BaseModelMapper<Film> {
    public FilmRowMapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpaId(getNullableLong(resultSet, "mpa_id"))
                .build();
    }

    @Override
    public List<String> getStoringFields() {
        Set<String> excluded = new HashSet<>(List.of(
                "likes",
                "mpa",
                "genres"
        ));
        return super
                .getStoringFields()
                .stream()
                .filter(field -> !excluded.contains(field))
                .toList();
    }

    public HashMap<String, Object> mapModel(Film model) {
        HashMap<String, Object> result = super.mapModel(model);
        result.put("mpa_id", model.getMpaId());
        return result;
    }

    public Film mapWithSaving(FilmSavingDto dto) {
        Film model = Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .duration(dto.getDuration())
                .releaseDate(dto.getReleaseDate())
                .description(dto.getDescription())
                .mpaId(
                        dto.getMpa() != null
                                ? dto.getMpa().getId()
                                : null
                )
                .build();

        if (
                dto.getGenres() != null
        ) {
            model
                    .getGenreIds()
                    .addAll(
                            dto
                                    .getGenres()
                                    .stream()
                                    .map(ModelDto::getId)
                                    .toList()
                    );
        }

        return model;
    }

    @Override
    protected Film getModelInstance() {
        return Film
                .builder()
                .build();
    }
}
