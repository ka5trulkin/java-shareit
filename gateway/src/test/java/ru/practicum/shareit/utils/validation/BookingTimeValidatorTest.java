package ru.practicum.shareit.utils.validation;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.booking.dto.BookingDTO;

import static org.junit.jupiter.api.Assertions.*;

class BookingTimeValidatorTest extends AbstractTest {
    private final BookingTimeValidator validator = new BookingTimeValidator();

    @Test
    void isValid() {
        assertTrue(validator.isValid(getBookingDTO(), null));
    }

    @Test
    void isValid_whenStartIsAfterEnd_thenReturnFalse() {
        final BookingDTO bookingDTO = getBookingDTO();
        bookingDTO.setStart(bookingDTO.getEnd().plusDays(1));

        assertFalse(validator.isValid(bookingDTO, null));
    }

    @Test
    void isValid_whenStartIsNull_thenReturnFalse() {
        final BookingDTO bookingDTO = getBookingDTO();
        bookingDTO.setStart(null);

        assertFalse(validator.isValid(bookingDTO, null));
    }

    @Test
    void isValid_whenEndIsNull_thenReturnFalse() {
        final BookingDTO bookingDTO = getBookingDTO();
        bookingDTO.setEnd(null);

        assertFalse(validator.isValid(bookingDTO, null));
    }
}