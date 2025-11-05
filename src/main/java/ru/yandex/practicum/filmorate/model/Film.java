package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.Marker;

import java.time.LocalDate;
import java.util.Collection;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    @Null(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnUpdate.class)
    Long id;
    @NotBlank(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnCreate.class)
    String name;
    @Size(max = 200)
    @NotNull(groups = Marker.OnCreate.class)
    String description;
    LocalDate releaseDate;
    @Positive
    Long duration;
    Mpa mpa;
    Collection<Genre> genres;
}
