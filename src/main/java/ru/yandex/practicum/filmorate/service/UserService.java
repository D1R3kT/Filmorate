package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final Map<Long, Set<Long>> friends = new HashMap<>();
    private final UserRepository userRepository;

    public void addFriend(Long userId, Long friendId) {
        if (userRepository.get(userId) == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        if (userRepository.get(friendId) == null) {
            throw new NotFoundException("Friend with id " + friendId + " not found");
        }

        Set<Long> userFriendsIds = friends.computeIfAbsent(userId, id -> new HashSet<>());
        userFriendsIds.add(friendId);
        friends.put(userId, userFriendsIds);

        Set<Long> friendFriendsIds = friends.computeIfAbsent(friendId, id -> new HashSet<>());
        friendFriendsIds.add(userId);
        friends.put(friendId, friendFriendsIds);
    }

    public void removeFriend(Long userId, Long friendId) {
        if (userRepository.get(userId) == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        if (userRepository.get(friendId) == null) {
            throw new NotFoundException("Friend with id " + friendId + " not found");
        }

        Set<Long> userFriendsIds = friends.computeIfAbsent(userId, id -> new HashSet<>());
        userFriendsIds.remove(friendId);
        friends.put(userId, userFriendsIds);

        Set<Long> friendFriendsIds = friends.computeIfAbsent(friendId, id -> new HashSet<>());
        friendFriendsIds.remove(userId);
        friends.put(friendId, friendFriendsIds);
    }

    public Set<User> getAllFriends(Long userId) {
        if (userRepository.get(userId) == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        Set<Long> userFriendsIds = friends.computeIfAbsent(userId, is -> new HashSet<>());
        Set<User> friends = new HashSet<>();

        for (Long id : userFriendsIds) {
            friends.add(userRepository.get(id));
        }
        return friends;
    }

    public Set<User> getCommonFriends(Long userId, Long friendId) {
        Set<Long> userFriendsIds = friends.get(userId);
        Set<Long> friendFriendsIds = friends.get(friendId);

        Set<User> commonFriends = new HashSet<>();
        for (long id : friendFriendsIds) {
            if (userFriendsIds.contains(id)) {
                commonFriends.add(userRepository.get(id));
            }
        }
        return commonFriends;
    }
}
