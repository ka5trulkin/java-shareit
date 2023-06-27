package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingOutDTO;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.service.CreateInfo;

import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
import static ru.practicum.shareit.booking.BookingLogMessage.*;
import static ru.practicum.shareit.service.PatternsApp.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(CREATED)
    BookingOutDTO createBooking(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                @Validated(CreateInfo.class) @RequestBody BookingDTO bookingDto) {
        log.info(REQUEST_ADD_BOOKING, bookingDto.getItemId());
        return bookingService.addBooking(bookerId, bookingDto);
    }

    @PatchMapping("{bookingId}")
    BookingOutDTO approvalBooking(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                  @PathVariable Long bookingId,
                                  @RequestParam boolean approved) {
        log.info(REQUEST_APPROVAL_BOOKING, bookingId);
        return bookingService.approvalBooking(ownerId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    BookingOutDTO getBookingByOwnerOrBooker(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                            @PathVariable Long bookingId) {
        log.info(REQUEST_GET_BOOKING, bookingId);
        return bookingService.getBookingByOwnerOrBooker(userId, bookingId);
    }

    @GetMapping
    Collection<BookingOutDTO> getBookingsByBooker(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
                                                  @RequestParam(defaultValue = "all") String state) {
        log.info(REQUEST_GET_BOOKING_LIST_BY_BOOKER, bookerId);
        return bookingService.getBookingsByBooker(bookerId, state);
    }

    @GetMapping("/owner")
    Collection<BookingOutDTO> getBookingsByOwner(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                                 @RequestParam(defaultValue = "all") String state) {
        log.info(REQUEST_GET_BOOKING_LIST_BY_OWNER, ownerId);
        return bookingService.getBookingsByOwner(ownerId, state);
    }
}