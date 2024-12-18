package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto addUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new EmailAlreadyExistsException("Пользователь с таким email уже существует");
        }

        User user = UserMapper.toUser(userDto);
        User createdUser = userRepository.addUser(user);
        return UserMapper.toUserDto(createdUser);
    }

    public UserDto updateUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new EmailAlreadyExistsException("Пользователь с таким email уже существует");
        }

        User user = UserMapper.toUser(userDto);
        User updatedUser = userRepository.updateUser(user);
        return UserMapper.toUserDto(updatedUser);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.getUserById(id);
        return UserMapper.toUserDto(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteUser(id);
    }
}
