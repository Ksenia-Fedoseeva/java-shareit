package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository inMemoryUserRepository;

    public User addUser(User user) {
        return inMemoryUserRepository.addUser(user);
    }

    public User updateUser(User user) {
        return inMemoryUserRepository.updateUser(user);
    }


    public User getUserById(Long id) {
        User user = inMemoryUserRepository.getUserById(id);
        return user;
    }

    public void deleteUserById(Long id) {
        inMemoryUserRepository.deleteUser(id);
    }
}
