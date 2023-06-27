package ru.practicum.shareit.booking.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingOutDTO;

import java.util.Collection;

@Transactional(readOnly = true)
public interface BookingService {
    @Transactional
    BookingOutDTO addBooking(Long bookerId, BookingDTO bookingDTO);

    @Transactional
    BookingOutDTO approvalBooking(Long ownerId, Long bookingId, boolean approved);

    BookingOutDTO getBookingByOwnerOrBooker(Long userId, Long bookingId);

    Collection<BookingOutDTO> getBookingsByBooker(Long bookerId, String stateParam);

    Collection<BookingOutDTO> getBookingsByOwner(Long ownerId, String stateParam);
}