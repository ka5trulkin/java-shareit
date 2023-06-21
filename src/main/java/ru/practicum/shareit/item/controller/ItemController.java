package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateInfo;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static ru.practicum.shareit.item.ItemLogMessage.*;

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
    Item createItem(@RequestHeader("X-Sharer-User-Id") long ownerId,
                    @Validated(CreateInfo.class) @RequestBody ItemDto itemDto) {
        log.info(REQUEST_ADD_ITEM.message(), itemDto.getName());
        return itemService.addItem(ownerId, itemDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    Item updateItem(@PathVariable long id,
                    @RequestHeader("X-Sharer-User-Id") long ownerId,
                    @RequestBody ItemDto itemDto) {
        log.info(REQUEST_UPDATE_ITEM.message(), id);
        return itemService.updateItem(id, ownerId, itemDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    Item getItemById(@PathVariable long id) {
        log.info(REQUEST_GET_ITEM.message(), id);
        return itemService.getItemById(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    Collection<Item> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info(REQUEST_GET_ITEM_LIST.message());
        return itemService.getItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    @ResponseStatus(OK)
    Collection<Item> getItemBySearch(@RequestParam String text) {
        log.info(REQUEST_GET_ITEM_BY_QUERY.message(), text);
        return itemService.getItemBySearch(text);
    }
}