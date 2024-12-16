package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {
    User getUserById(Long id);

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    User findByEmail(String email);
}
