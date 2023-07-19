package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemWithBookingInfoDTO;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.service.CreateInfo;
import ru.practicum.shareit.service.UpdateInfo;

import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
import static ru.practicum.shareit.item.ItemLogMessage.*;
import static ru.practicum.shareit.service.PatternsApp.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(CREATED)
    ItemDTO createItem(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                       @Validated(CreateInfo.class) @RequestBody ItemDTO itemDTO) {
        log.info(REQUEST_ADD_ITEM, itemDTO.getName());
        return itemService.addItem(ownerId, itemDTO);
    }

    @PatchMapping("/{id}")
    ItemDTO updateItem(@PathVariable Long id,
                       @RequestHeader(X_SHARER_USER_ID) Long ownerId,
                       @Validated(UpdateInfo.class) @RequestBody ItemDTO itemDTO) {
        log.info(REQUEST_UPDATE_ITEM, id);
        return itemService.updateItem(id, ownerId, itemDTO);
    }

    @GetMapping("/{id}")
    ItemDTO getItemById(@RequestHeader(X_SHARER_USER_ID) Long userId, @PathVariable Long id) {
        log.info(REQUEST_GET_ITEM, id);
        return itemService.getItemById(id, userId);
    }

    @GetMapping
    Collection<ItemWithBookingInfoDTO> getItemsByOwner(@RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        log.info(REQUEST_GET_ITEM_LIST);
        return itemService.getItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    Collection<ItemWithBookingInfoDTO> getItemBySearch(@RequestParam String text) {
        log.info(REQUEST_GET_ITEM_BY_QUERY, text);
        return itemService.getItemsBySearch(text);
    }

    @PostMapping("/{itemId}/comment")
    CommentDTO createComment(@RequestHeader(X_SHARER_USER_ID) Long authorId,
                          @PathVariable Long itemId,
                          @Validated(CreateInfo.class) @RequestBody CommentDTO commentDTO) {
        log.info(REQUEST_ADD_COMMENT, authorId);
        return itemService.addComment(authorId, itemId, commentDTO);
    }
}