package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmRepository;
import ru.yandex.practicum.filmorate.storage.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;

@Component
@RequiredArgsConstructor
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final Map<Long, Set<Long>> likes = new HashMap<>();
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    public void addLike(Long filmId, Long userId) {
        if (filmRepository.get(filmId) == null) {
            throw new NotFoundException("Film with id " + filmId + " not found");
        }
        if (userRepository.get(userId) == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        Set<Long> usersLike = likes.computeIfAbsent(filmId, id -> new HashSet<>());
        usersLike.add(userId);
        likes.put(filmId, usersLike);
    }

    public void removeLike(Long filmId, Long userId) {
        if (filmRepository.get(filmId) == null) {
            throw new NotFoundException("Film with id " + filmId + " not found");
        }
        if (userRepository.get(userId) == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        Set<Long> usersLike = likes.computeIfAbsent(filmId, id -> new HashSet<>());
        usersLike.remove(userId);
        likes.put(filmId, usersLike);
    }

    public Collection<Film> getTopFilms(int count) {
        if (count < 0) {
            throw new ValidationException("Count is negative");
        }
        return likes.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getValue().size(), reverseOrder()))
                .map(entry -> filmRepository.get(entry.getKey()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
