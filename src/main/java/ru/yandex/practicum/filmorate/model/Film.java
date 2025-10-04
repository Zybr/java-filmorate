package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.antlr.v4.runtime.misc.OrderedHashSet;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class Film extends Model {
    protected Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200)
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past
    private LocalDate releaseDate;

    @NotNull
    @Positive
    private Integer duration;

    @EqualsAndHashCode.Exclude
    private final Set<Long> likes = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @Null
    @JsonIgnore
    private Long mpaId;

    @EqualsAndHashCode.Exclude
    @Null
    private Mpa mpa;

    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private final Set<Long> genreIds = new HashSet<>();

    @EqualsAndHashCode.Exclude
    private final Set<Genre> genres = new OrderedHashSet<>();
}
