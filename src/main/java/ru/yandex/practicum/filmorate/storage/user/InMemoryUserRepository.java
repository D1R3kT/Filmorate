package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ConditionsMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service

public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("email is null or not found @");
            throw new ConditionsMetException("Error: email is null or not found @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("login is null");
            throw new ConditionsMetException("Error: login is null");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Birthday in the future");
            throw new ConditionsMetException("Error: Birthday in the future");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("The user has been saved: " + user);
        return user;
    }

    @Override
    public void remove(Long id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else throw new NotFoundException("User with id = " + id + "not found");
    }

    @Override
    public Collection<User> getAll() {
        log.info("return all users");
        return users.values();
    }

    @Override
    public User get(Long id) {
        return users.get(id);
    }

    @Override
    public User update(User newUser) {
        if (users.containsKey(newUser.getId())) {
            User user = users.get(newUser.getId());

            if (newUser.getEmail() != null) user.setEmail(newUser.getEmail());
            if (newUser.getName() != null) user.setName(newUser.getName());
            if (newUser.getLogin() != null) user.setLogin(newUser.getLogin());
            if (newUser.getBirthday() != null && newUser.getBirthday().isBefore(LocalDate.now())) {
                user.setBirthday(newUser.getBirthday());
            }
            log.info("update user: {}", user);
            return user;
        } else {
            log.error("user with id {} not found", newUser.getId());
            throw new NotFoundException("user not found");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
