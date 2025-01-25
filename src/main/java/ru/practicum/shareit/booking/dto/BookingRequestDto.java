package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.annotations.ValidBookingDates;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidBookingDates
public class BookingRequestDto {
    @NotNull(message = "Дата начала бронирования обязательна")
    @Future(message = "Дата начала бронирования должна быть в будущем")
    private LocalDateTime start;

    @NotNull(message = "Дата окончания бронирования обязательна")
    @Future(message = "Дата окончания бронирования должна быть в будущем")
    private LocalDateTime end;

    @NotNull(message = "ID вещи обязательно")
    private Long itemId;
}
