package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.utils.CreateInfo;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.booking.utils.BookingLogMessage.*;
import static ru.practicum.shareit.utils.PatternsApp.BOOKINGS_PREFIX;
import static ru.practicum.shareit.utils.PatternsApp.X_SHARER_USER_ID;

@Controller
@RequestMapping(path = BOOKINGS_PREFIX)
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	ResponseEntity<Object> createBooking(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
								@Validated(CreateInfo.class) @RequestBody BookingDTO bookingDto) {
		log.info(REQUEST_ADD_BOOKING, bookingDto.getItemId());
		return bookingClient.addBooking(bookerId, bookingDto);
	}

	@PatchMapping("/{bookingId}")
	ResponseEntity<Object> approvalBooking(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
								  @PathVariable Long bookingId,
								  @RequestParam boolean approved) {
		log.info(REQUEST_APPROVAL_BOOKING, bookingId);
		return bookingClient.approvalBooking(ownerId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	ResponseEntity<Object> getBookingByOwnerOrBooker(@RequestHeader(X_SHARER_USER_ID) Long userId,
											@PathVariable Long bookingId) {
		log.info(REQUEST_GET_BOOKING, bookingId);
		return bookingClient.getBookingByOwnerOrBooker(userId, bookingId);
	}

	@GetMapping
	ResponseEntity<Object> getBookingsByBooker(@RequestHeader(X_SHARER_USER_ID) Long bookerId,
											@RequestParam(defaultValue = "all") String state,
											@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
											@RequestParam(defaultValue = "10") @Positive Integer size) {
		log.info(REQUEST_GET_BOOKING_LIST_BY_BOOKER, bookerId);
		return bookingClient.getBookingsByBooker(bookerId, state, from, size);
	}

	@GetMapping("/owner")
	ResponseEntity<Object> getBookingsByOwner(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
										   @RequestParam(defaultValue = "all") String state,
										   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
										   @RequestParam(defaultValue = "10") @Positive Integer size) {
		log.info(REQUEST_GET_BOOKING_LIST_BY_OWNER, ownerId);
		return bookingClient.getBookingsByOwner(ownerId, state, from, size);
	}
}