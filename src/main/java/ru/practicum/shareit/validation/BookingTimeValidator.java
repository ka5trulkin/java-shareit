package ru.practicum.shareit.validation;

import ru.practicum.shareit.booking.dto.BookingDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingTimeValidator implements ConstraintValidator<BookingTime, BookingDTO> {
    @Override
    public boolean isValid(BookingDTO bookingDTO, ConstraintValidatorContext constraintValidatorContext) {
        final LocalDateTime start = bookingDTO.getStart();
        final LocalDateTime end = bookingDTO.getEnd();
        if (start != null && end != null) {
            return bookingDTO.getStart().isBefore(bookingDTO.getEnd());
        }
        return false;
    }
}