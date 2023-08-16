package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingOutDTO;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static ru.practicum.shareit.booking.BookingLogMessage.*;
import static ru.practicum.shareit.utils.PatternsApp.BOOKINGS_PREFIX;
import static ru.practicum.shareit.utils.PatternsApp.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping(path = BOOKINGS_PREFIX)
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(CREATED)
    BookingOutDTO createBooking(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                @RequestBody BookingDTO bookingDto) {
        log.info(REQUEST_ADD_BOOKING, bookingDto.getItemId());
        return bookingService.addBooking(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    BookingOutDTO approvalBooking(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                  @PathVariable Long bookingId,
                                  @RequestParam boolean approved) {
        log.info(REQUEST_APPROVAL_BOOKING, bookingId);
        return bookingService.approvalBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingOutDTO getBookingByOwnerOrBooker(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                            @PathVariable Long bookingId) {
        log.info(REQUEST_GET_BOOKING, bookingId);
        return bookingService.getBookingByOwnerOrBooker(userId, bookingId);
    }

    @GetMapping
    List<BookingOutDTO> getBookingsByBooker(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                            @RequestParam String state,
                                            @RequestParam Integer from,
                                            @RequestParam Integer size) {
        log.info(REQUEST_GET_BOOKING_LIST_BY_BOOKER, bookerId);
        return bookingService.getBookingsByBooker(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    List<BookingOutDTO> getBookingsByOwner(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                           @RequestParam String state,
                                           @RequestParam Integer from,
                                           @RequestParam Integer size) {
        log.info(REQUEST_GET_BOOKING_LIST_BY_OWNER, ownerId);
        return bookingService.getBookingsByOwner(ownerId, state, from, size);
    }
}