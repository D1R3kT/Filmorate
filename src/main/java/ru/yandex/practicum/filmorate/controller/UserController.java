package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Marker;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {
    HashMap<Long, User> storage = new HashMap<>();

    Long id = 0L;

    public Long createId() {
        ++id;
        return id;
    }

    @GetMapping
    public Collection<User> getAll(){
        log.info("get all Users: {}", storage.values());
        return storage.values();
    }

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public User create(@RequestBody @Valid User user) {
        log.info("create User: {} - Started", user);
        user.setId(createId());
        if(user.getName() == null) {
            user.setName(user.getLogin());
        }
        storage.put(user.getId(), user);
        log.info("create User: {} - Finished", user);
        return user;
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public User update(@RequestBody @Valid User user) {
        log.info("update user {} - Start", user);
        if (storage.containsKey(user.getId())) {
            User oldUser = storage.get(user.getId());
            if (user.getEmail() != null && !user.getEmail().isBlank()) {
                oldUser.setEmail(user.getEmail());
            }
            if (user.getLogin() != null && !user.getLogin().isBlank()) {
                oldUser.setLogin(user.getLogin());
            }
            if (user.getName() != null && !user.getName().isBlank()) {
                oldUser.setName(user.getName());
            }
            if (user.getBirthday() != null) {
                oldUser.setBirthday(user.getBirthday());
            }
            log.info("update user {} - Finished", user);
            return oldUser;
        } else {
            log.info("User with id = " + user.getId() + " not found");
            throw new NotFoundException("User with id = " + user.getId() + " not found");
        }
    }

}
