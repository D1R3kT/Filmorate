package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmRepository;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private final InMemoryFilmRepository filmRepository;

    @GetMapping
    public Collection<Film> getAll() {
        return filmRepository.getAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmRepository.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        return filmRepository.update(newFilm);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        filmRepository.delete(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getTopFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
    }

}
