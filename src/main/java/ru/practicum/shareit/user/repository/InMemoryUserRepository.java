package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
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
        if (findByEmail(user.getEmail()) != null) {
            throw new EmailAlreadyExistsException("Пользователь с таким email уже существует");
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (findByEmail(user.getEmail()) != null) {
            throw new EmailAlreadyExistsException("Пользователь с таким email уже существует");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

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
