package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Marker;
import ru.yandex.practicum.filmorate.exceptions.ConditionsMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Validated
@Slf4j
public class FilmController {
    HashMap<Long, Film> storage = new HashMap<>();
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1985, 12, 28);
    Long id = 0L;

    public Long createId() {
        return ++id;
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("get all Films: {}", storage.values());
        return storage.values();
    }

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public Film create(@RequestBody  @Valid Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Release date is before 1895");
            throw new ConditionsMetException("дата релиза - не раньше 28 декабря 1895 года");
        }
        log.info("create Film: {} - Started", film);
        film.setId(createId());
        storage.put(film.getId(), film);
        log.info("create Film: {} - Finished", film);
        return film;
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public Film update(@RequestBody @Valid Film film) {
        if (storage.containsKey(film.getId())) {
            Film oldFilm = storage.get(film.getId());
            if (film.getName() != null && !film.getName().isBlank()) {
                oldFilm.setName(film.getName());
            }
            if (film.getDescription() != null && !film.getDescription().isBlank()) {
                oldFilm.setDescription(film.getDescription());
            }
            if (film.getDuration() != null && film.getDuration() > 0) {
                oldFilm.setDuration(film.getDuration());
            }
            if (film.getReleaseDate() != null && !isValidReleaseDate(film.getReleaseDate())) {
                oldFilm.setReleaseDate(film.getReleaseDate());
            }
            log.info("update Film : {} - Finished", oldFilm);
            return oldFilm;
        } else {
            log.info("update Film has error: Film with id = {} not found", film.getId());
            throw new NotFoundException("Film with id = " + film.getId() + " not found");
        }
    }

    @AssertTrue(message = "Release date invalid")
    public boolean isValidReleaseDate(LocalDate releaseDate) {
        return releaseDate.isBefore(FILM_BIRTHDAY);
    }

}
