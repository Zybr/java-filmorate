package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class Film extends AbstractModel {
    protected Long id;
    private String name;
    private String description;
    private String releaseDate;
    private Integer duration;
}
