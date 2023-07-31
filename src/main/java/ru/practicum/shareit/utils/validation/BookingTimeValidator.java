package ru.practicum.shareit.utils.validation;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

@Service
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