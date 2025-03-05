package ru.practicum.shareit.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BookingDatesValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBookingDates {
    String message() default "Дата начала бронирования должна быть раньше даты окончания";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}