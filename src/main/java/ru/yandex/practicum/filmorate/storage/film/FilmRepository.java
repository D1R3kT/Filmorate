package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmRepository {
    Collection<Film> getAll();

    Film get(Long id);

    Film create(Film film);

    Film update(Film newFilm);

    void delete(Long id);
}
