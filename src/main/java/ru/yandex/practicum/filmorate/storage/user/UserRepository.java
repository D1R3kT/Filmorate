package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserRepository {

    Collection<User> getAll();

    User get(Long id);

    User create(User user);

    User update(User user);

    void remove(Long id);

}
