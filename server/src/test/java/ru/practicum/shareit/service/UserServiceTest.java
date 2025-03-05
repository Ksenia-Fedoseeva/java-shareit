package ru.practicum.shareit.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void testAddUser() {
        UserDto userDto = new UserDto(null, "Alice", "alice@example.com");
        UserDto savedUser = userService.addUser(userDto);

        assertNotNull(savedUser.getId());
        assertEquals("Alice", savedUser.getName());
        assertEquals("alice@example.com", savedUser.getEmail());
    }

    @Test
    void testUpdateUser() {
        UserDto userDto = userService.addUser(new UserDto(null, "Bob", "bob@example.com"));
        userDto.setName("Bobby");
        userService.updateUser(userDto);

        UserDto updatedUser = userService.getUserDtoById(userDto.getId());

        assertEquals("Bobby", updatedUser.getName());
    }

    @Test
    void testDeleteUser() {
        UserDto userDto = userService.addUser(new UserDto(null, "Charlie", "charlie@example.com"));
        userService.deleteUserById(userDto.getId());

        assertThrows(NotFoundException.class, () -> userService.getUserDtoById(userDto.getId()));
    }
}