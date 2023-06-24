package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.service.CreateInfo;

import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
import static ru.practicum.shareit.item.ItemLogMessage.*;
import static ru.practicum.shareit.service.PatternsApp.X_SHARER_USER_ID;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(CREATED)
    ItemDto createItem(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                    @Validated(CreateInfo.class) @RequestBody ItemDto itemDto) {
        log.info(REQUEST_ADD_ITEM, itemDto.getName());
        return itemService.addItem(ownerId, itemDto);
    }

    @PatchMapping("/{id}")
    ItemDto updateItem(@PathVariable Long id,
                    @RequestHeader(X_SHARER_USER_ID) Long ownerId,
                    @RequestBody ItemDto itemDto) {
        log.info(REQUEST_UPDATE_ITEM, id);
        return itemService.updateItem(id, ownerId, itemDto);
    }

    @GetMapping("/{id}")
    ItemDto getItemById(@PathVariable Long id) {
        log.info(REQUEST_GET_ITEM, id);
        return itemService.getItemById(id);
    }

    @GetMapping
    Collection<ItemDto> getItemsByOwner(@RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        log.info(REQUEST_GET_ITEM_LIST);
        return itemService.getItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    Collection<ItemDto> getItemBySearch(@RequestParam String text) {
        log.info(REQUEST_GET_ITEM_BY_QUERY, text);
        return itemService.getItemBySearch(text);
    }
}