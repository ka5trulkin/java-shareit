package ru.practicum.shareit.exception.booking;

import ru.practicum.shareit.exception.base.NotFoundException;

import static ru.practicum.shareit.exception.base.ExceptionMessage.BOOKING_NOT_FOUND;

public class BookingNotFoundException extends NotFoundException {
    public BookingNotFoundException(Long bookingId) {
        super(String.format(BOOKING_NOT_FOUND, bookingId));
    }
}