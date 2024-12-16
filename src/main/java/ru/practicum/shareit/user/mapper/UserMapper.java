package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserDto toUserDto (User user) {
        if (user != null) {
            return new UserDto(
                    user.getId(),
                    user.getName(),
                    user.getEmail());
        }
        return null;
    }
}
