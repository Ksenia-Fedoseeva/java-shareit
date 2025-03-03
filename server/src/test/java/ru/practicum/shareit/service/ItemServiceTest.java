package ru.practicum.shareit.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class ItemServiceTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;

    @Test
    void testCreateItem() {
        UserDto user = userService.addUser(new UserDto(null, "Dave", "dave@example.com"));
        ItemDto itemDto = new ItemDto(null, "Drill", "Cordless drill", true, null);

        ItemDto savedItem = itemService.createItem(itemDto, user.getId());

        assertNotNull(savedItem.getId());
        assertEquals("Drill", savedItem.getName());
        assertEquals("Cordless drill", savedItem.getDescription());
    }

    @Test
    void testUpdateItem() {
        UserDto user = userService.addUser(new UserDto(null, "Eve", "eve@example.com"));
        ItemDto itemDto = itemService.createItem(new ItemDto(null, "Saw", "Electric saw",
                true, null), user.getId());

        itemDto.setName("Chainsaw");
        itemService.updateItem(itemDto.getId(), itemDto, user.getId());

        ItemResponseDto updatedItem = itemService.getItemById(itemDto.getId(), user.getId());

        assertEquals("Chainsaw", updatedItem.getName());
    }

    @Test
    void testSearchItems() {
        UserDto user = userService.addUser(new UserDto(null, "Frank", "frank@example.com"));
        itemService.createItem(new ItemDto(null, "Table", "Wooden table", true, null),
                user.getId());

        List<ItemDto> results = itemService.searchItems("table");

        assertFalse(results.isEmpty());
        assertEquals("Table", results.get(0).getName());
    }

    @Test
    void testAddComment() {
        UserDto owner = userService.addUser(new UserDto(null, "Owner", "owner@example.com"));

        UserDto booker = userService.addUser(new UserDto(null, "Booker", "booker@example.com"));

        ItemDto item = itemService.createItem(new ItemDto(null, "Laptop", "Gaming Laptop",
                true, null), owner.getId());

        LocalDateTime start = LocalDateTime.now().minusDays(5);
        LocalDateTime end = LocalDateTime.now().minusDays(1);

        BookingRequestDto bookingRequest = new BookingRequestDto(start, end, item.getId());
        BookingResponseDto booking = bookingService.createBooking(booker.getId(), bookingRequest);

        bookingService.approveBooking(booking.getId(), owner.getId(), true);

        CommentRequestDto commentRequest = new CommentRequestDto("Отличный ноутбук!");
        CommentResponseDto comment = itemService.addComment(booker.getId(), item.getId(), commentRequest);

        assertNotNull(comment);
        assertEquals("Отличный ноутбук!", comment.getText());
    }
}
