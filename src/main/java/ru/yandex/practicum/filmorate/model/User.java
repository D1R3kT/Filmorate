package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.Marker;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @NotNull(groups = Marker.OnUpdate.class)
    Long id;
    @Email
    @NotNull(groups = Marker.OnCreate.class)
    String email;
    @NotNull(groups = Marker.OnCreate.class)
    @Pattern(regexp = "^[^\\s]+$")
    String login;
    String name;
    @Past(groups = Marker.OnCreate.class)
    LocalDate birthday;
}
