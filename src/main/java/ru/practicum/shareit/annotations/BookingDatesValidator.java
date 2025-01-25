package ru.practicum.shareit.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

public class BookingDatesValidator implements ConstraintValidator<ValidBookingDates, BookingRequestDto> {

    @Override
    public boolean isValid(BookingRequestDto dto, ConstraintValidatorContext context) {
        return dto.getStart().isBefore(dto.getEnd());
    }
}
