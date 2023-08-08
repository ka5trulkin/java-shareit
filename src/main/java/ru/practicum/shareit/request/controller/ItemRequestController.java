package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestOutWithItems;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static ru.practicum.shareit.request.ItemRequestMessage.*;
import static ru.practicum.shareit.utils.PatternsApp.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(CREATED)
    ItemRequestDTO createItemRequest(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                     @Valid @RequestBody ItemRequestCreate request) {
        log.info(REQUEST_ADD_ITEM_REQUEST, request.getDescription());
        return itemRequestService.addRequest(ownerId, request);
    }

    @GetMapping
    List<ItemRequestOutWithItems> getAllToOwner(@RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        log.info(REQUEST_REQUESTS_BY_OWNER, ownerId);
        return itemRequestService.getAllToOwner(ownerId);
    }

    @GetMapping("/all")
    List<ItemRequestOutWithItems> getAll(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info(REQUEST_REQUESTS_BY_USER, userId);
        return itemRequestService.getAll(userId, from, size);
    }

    @GetMapping("{requestId}")
    ItemRequestOutWithItems getRequest(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                       @PathVariable Long requestId) {
        log.info(REQUEST_GET_REQUEST, requestId);
        return itemRequestService.getRequest(userId, requestId);
    }
}