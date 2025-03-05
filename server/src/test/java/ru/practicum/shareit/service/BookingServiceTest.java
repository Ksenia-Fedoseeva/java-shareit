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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

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
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void testCreateBooking() {
        User owner = userRepository.save(new User(null, "Gina", "gina@example.com"));
        User booker = userRepository.save(new User(null, "Hank", "hank@example.com"));

        Item item = itemRepository.save(new Item(null, "Bike", "Mountain bike", true,
                owner, null));

        BookingRequestDto bookingRequest = new BookingRequestDto(LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());
        BookingResponseDto booking = bookingService.createBooking(booker.getId(), bookingRequest);

        assertNotNull(booking.getId());
        assertEquals(Status.WAITING, booking.getStatus());
    }

    @Test
    void testApproveBooking() {
        User owner = userRepository.save(new User(null, "Ivy", "ivy@example.com"));
        User booker = userRepository.save(new User(null, "Jake", "jake@example.com"));

        Item item = itemRepository.save(new Item(null, "Laptop", "Gaming laptop", true,
                owner, null));

        BookingRequestDto bookingRequest = new BookingRequestDto(LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), item.getId());
        BookingResponseDto booking = bookingService.createBooking(booker.getId(), bookingRequest);

        BookingResponseDto approvedBooking = bookingService.approveBooking(booking.getId(), owner.getId(), true);

        assertEquals(Status.APPROVED, approvedBooking.getStatus());
    }
}
