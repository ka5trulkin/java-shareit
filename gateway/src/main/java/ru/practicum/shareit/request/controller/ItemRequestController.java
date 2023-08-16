package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestCreate;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.request.utils.ItemRequestMessage.*;
import static ru.practicum.shareit.utils.PatternsApp.ITEM_REQUEST_PREFIX;
import static ru.practicum.shareit.utils.PatternsApp.X_SHARER_USER_ID;

@Controller
@RequestMapping(path = ITEM_REQUEST_PREFIX)
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
	private final ItemRequestClient requestClient;

	@PostMapping
	ResponseEntity<Object> createItemRequest(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
									 @Valid @RequestBody ItemRequestCreate request) {
		log.info(REQUEST_ADD_ITEM_REQUEST, request.getDescription());
		return requestClient.addRequest(ownerId, request);
	}

	@GetMapping
	ResponseEntity<Object> getAllToOwner(@RequestHeader(X_SHARER_USER_ID) Long ownerId) {
		log.info(REQUEST_REQUESTS_BY_OWNER, ownerId);
		return requestClient.getAllToOwner(ownerId);
	}

	@GetMapping("/all")
	ResponseEntity<Object> getAll(@RequestHeader(X_SHARER_USER_ID) Long userId,
										 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
										 @RequestParam(defaultValue = "10") @Positive Integer size) {
		log.info(REQUEST_REQUESTS_BY_USER, userId);
		return requestClient.getAll(userId, from, size);
	}

	@GetMapping("{requestId}")
	ResponseEntity<Object> getRequest(@RequestHeader(X_SHARER_USER_ID) Long userId,
									   @PathVariable Long requestId) {
		log.info(REQUEST_GET_REQUEST, requestId);
		return requestClient.getRequest(userId, requestId);
	}
}