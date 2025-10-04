package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class Genre extends Model {
    protected Long id;

    @NotBlank
    private String name;
}
