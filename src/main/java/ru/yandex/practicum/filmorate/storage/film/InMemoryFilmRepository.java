package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ConditionsMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmRepository implements FilmRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmRepository.class);
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film get(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException("Film not found");
        }
    }

    @Override
    public Film create(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("name is null");
            throw new ConditionsMetException("name is null");
        }
        if (film.getDescription().length() > 200) {
            log.error("Description length > 200");
            throw new ConditionsMetException("Description length > 200");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Release date is before 1895");
            throw new ConditionsMetException("Release date is before 1895");
        }
        if (film.getDuration() < 0) {
            log.error("Duration is negative");
            throw new ConditionsMetException("Duration is negative");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Add film: {}", film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            Film film = films.get(newFilm.getId());

            if (newFilm.getName() != null || !newFilm.getName().isBlank()) {
                film.setName(newFilm.getName());
            }
            if (newFilm.getDescription() != null && newFilm.getDescription().length() <= 200) {
                film.setDescription(newFilm.getDescription());
            }
            if (newFilm.getDuration() != null && newFilm.getDuration() > 0) {
                film.setDuration(newFilm.getDuration());
            }
            if (newFilm.getReleaseDate() != null & newFilm.getReleaseDate().isAfter(
                    LocalDate.of(1985, 12, 28))) {
                film.setReleaseDate(newFilm.getReleaseDate());
            }
            log.info("Update film: {}", film);
            return film;
        } else {
            throw new NotFoundException("Film not found");
        }
    }

    @Override
    public void delete(Long id) {
        if (films.containsKey(id)) {
            films.remove(id);
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
