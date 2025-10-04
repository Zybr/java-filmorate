package ru.yandex.practicum.filmorate.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FilmSavingDto {
    private Long id;

    @NotBlank
    @Size(max = 50)
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

    @Valid
    private ModelDto mpa;

    @Valid
    private List<ModelDto> genres;
}
