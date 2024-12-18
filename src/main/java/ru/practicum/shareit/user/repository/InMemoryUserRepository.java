package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User getUserById(Long id) {
        User user = users.get(id);

        if (user == null) {
            throw new NotFoundException("Пользователь с ID " + id + " не найден");
        }

        return user;
    }

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    @Override
    public User findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail() != null)
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    private long getNextId() {
        return users.keySet().stream().mapToLong(id -> id).max().orElse(0) + 1;
    }

}
