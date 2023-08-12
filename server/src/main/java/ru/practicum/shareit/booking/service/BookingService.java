package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingOutDTO;

import java.util.List;

public interface BookingService {
    BookingOutDTO addBooking(Long bookerId, BookingDTO bookingDTO);

    BookingOutDTO approvalBooking(Long ownerId, Long bookingId, boolean approved);

    BookingOutDTO getBookingByOwnerOrBooker(Long userId, Long bookingId);

    List<BookingOutDTO> getBookingsByBooker(Long bookerId, String stateParam, Integer from, Integer size);

    List<BookingOutDTO> getBookingsByOwner(Long ownerId, String stateParam, Integer from, Integer size);
}