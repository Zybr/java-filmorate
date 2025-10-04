package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Like extends Model {
    protected Long id;

    @NotBlank
    @Positive
    private Long userId;

    @NotBlank
    @Positive
    private Long filmId;
}
