package ru.practicum.shareit.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class ItemRequestServiceTest {
    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private UserService userService;

    @Test
    void testCreateItemRequest() {
        UserDto user = userService.addUser(new UserDto(null, "Kyle", "kyle@example.com"));
        ItemRequestDto requestDto = new ItemRequestDto("Need a tent");

        ItemRequestResponseDto savedRequest = itemRequestService.createItemRequest(requestDto, user.getId());

        assertNotNull(savedRequest.getId());
        assertEquals("Need a tent", savedRequest.getDescription());
    }

    @Test
    void testGetUserRequests() {
        UserDto user = userService.addUser(new UserDto(null, "Liam", "liam@example.com"));
        itemRequestService.createItemRequest(new ItemRequestDto("Need a backpack"), user.getId());

        List<ItemRequestResponseDto> requests = itemRequestService.getUserRequests(user.getId());

        assertFalse(requests.isEmpty());
        assertEquals("Need a backpack", requests.get(0).getDescription());
    }
}
