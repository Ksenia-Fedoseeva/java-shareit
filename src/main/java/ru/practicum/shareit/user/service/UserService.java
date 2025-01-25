package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto addUser(UserDto userDto) {

        checkExistByEmail(userDto);

        User user = UserMapper.toUser(userDto);
        User createdUser = userRepository.save(user);
        return UserMapper.toUserDto(createdUser);
    }

    @Transactional
    public UserDto updateUser(UserDto userDto) {
        checkExistByEmail(userDto);

        userRepository.updateUser(userDto.getId(), userDto.getName(), userDto.getEmail());

        return getUserDtoById(userDto.getId());
    }

    private void checkExistByEmail(UserDto userDto) {
        if (userDto.getEmail() != null) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new EmailAlreadyExistsException("Пользователь с таким email уже существует");
            }
        }
    }

    public UserDto getUserDtoById(Long id) {
        User user = getUserById(id);
        return UserMapper.toUserDto(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
