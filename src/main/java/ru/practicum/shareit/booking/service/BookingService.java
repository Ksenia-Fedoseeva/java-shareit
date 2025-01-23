package ru.practicum.shareit.booking.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingResponseDto createBooking(Long userId, BookingRequestDto bookingRequestDto) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id " + bookingRequestDto.getItemId() + " не найдена"));

        if (item.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Вы не можете забронировать свою же вещь");
        }

        if (!item.getAvailable()) {
            throw new ValidationException("Эта вещь сейчас недоступна для бронирования");
        }

        Booking booking = BookingMapper.toBooking(bookingRequestDto, item);
        booking.setBooker(booker);
        booking.setStatus(Status.WAITING);

        booking = bookingRepository.save(booking);

        return BookingMapper.toResponseDto(booking);
    }

    @Transactional
    public BookingResponseDto approveBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование c id " + bookingId + " не найдено"));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Вы не являетесь владельцем вещи");
        }

        Status newStatus = approved ? Status.APPROVED : Status.REJECTED;

        booking.setStatus(newStatus);

        booking = bookingRepository.save(booking);

        return BookingMapper.toResponseDto(booking);
    }

    public BookingResponseDto getBookingById(Long bookingId, Long userId) {
        /*if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }*/

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование c id " + bookingId + " не найдено"));

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Вы не владелец вещи и не автор бронирования");
        }

        return BookingMapper.toResponseDto(booking);
    }

    public List<BookingResponseDto> getBookings(Long userId, String state, Boolean forOwner) {
        /*if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }*/

        BookingState bookingState = BookingState.fromString(state);

        List<Booking> bookings = forOwner
                ? bookingRepository.findBookingsForOwner(userId)
                : bookingRepository.findBookingsForBooker(userId);

        LocalDateTime now = LocalDateTime.now();
        bookings = bookings.stream()
                .filter(booking -> filterByState(booking, bookingState, now))
                .collect(Collectors.toList());

        return bookings.stream().map(BookingMapper::toResponseDto).collect(Collectors.toList());
    }

    private Boolean filterByState(Booking booking, BookingState state, LocalDateTime now) {
        return switch (state) {
            case CURRENT -> booking.getStart().isBefore(now) && booking.getEnd().isAfter(now);
            case PAST -> booking.getEnd().isBefore(now);
            case FUTURE -> booking.getStart().isAfter(now);
            case WAITING -> booking.getStatus() == Status.WAITING;
            case REJECTED -> booking.getStatus() == Status.REJECTED;
            default -> true;
        };
    }
}
