package ru.practicum.shareit.booking.enums;

import jakarta.validation.ValidationException;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState fromString(String state) {
        try {
            return BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Неизвестный параметр state: " + state);
        }
    }
}
