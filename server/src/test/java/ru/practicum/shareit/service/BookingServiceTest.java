package ru.practicum.shareit.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class BookingServiceTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    @Test
    void testCreateBooking() {
        UserDto owner = userService.addUser(new UserDto(null, "Gina", "gina@example.com"));
        UserDto booker = userService.addUser(new UserDto(null, "Hank", "hank@example.com"));

        ItemDto item = itemService.createItem(new ItemDto(null, "Bike", "Mountain bike",
                true, null), owner.getId());

        BookingRequestDto bookingRequest = new BookingRequestDto(LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());
        BookingResponseDto booking = bookingService.createBooking(booker.getId(), bookingRequest);

        assertNotNull(booking.getId());
        assertEquals(Status.WAITING, booking.getStatus());
    }

    @Test
    void testApproveBooking() {
        UserDto owner = userService.addUser(new UserDto(null, "Ivy", "ivy@example.com"));
        UserDto booker = userService.addUser(new UserDto(null, "Jake", "jake@example.com"));

        ItemDto item = itemService.createItem(new ItemDto(null, "Laptop", "Gaming laptop",
                true, null), owner.getId());

        BookingRequestDto bookingRequest = new BookingRequestDto(LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());
        BookingResponseDto booking = bookingService.createBooking(booker.getId(), bookingRequest);

        BookingResponseDto approvedBooking = bookingService.approveBooking(booking.getId(), owner.getId(), true);

        assertEquals(Status.APPROVED, approvedBooking.getStatus());
    }
}
