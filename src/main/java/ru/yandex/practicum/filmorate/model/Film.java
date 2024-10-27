package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Duration duration;
    private int likesCount;
}
