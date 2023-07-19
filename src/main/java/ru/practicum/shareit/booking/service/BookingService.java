package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingOutDTO;

import java.util.Collection;

public interface BookingService {
    BookingOutDTO addBooking(Long bookerId, BookingDTO bookingDTO);

    BookingOutDTO approvalBooking(Long ownerId, Long bookingId, boolean approved);

    BookingOutDTO getBookingByOwnerOrBooker(Long userId, Long bookingId);

    Collection<BookingOutDTO> getBookingsByBooker(Long bookerId, String stateParam);

    Collection<BookingOutDTO> getBookingsByOwner(Long ownerId, String stateParam);
}