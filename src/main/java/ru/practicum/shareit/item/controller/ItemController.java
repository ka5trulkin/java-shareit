package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.item.dto.item.ItemOut;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.utils.CreateInfo;
import ru.practicum.shareit.utils.UpdateInfo;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static ru.practicum.shareit.item.ItemLogMessage.*;
import static ru.practicum.shareit.utils.PatternsApp.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
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
    ItemOut getItemById(@RequestHeader(X_SHARER_USER_ID) Long userId, @PathVariable Long id) {
        log.info(REQUEST_GET_ITEM, id);
        return itemService.getItemById(id, userId);
    }

    @GetMapping
    List<ItemOut> getItemsByOwner(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info(REQUEST_GET_ITEM_LIST);
        return itemService.getItemsByOwner(ownerId, from, size);
    }

    @GetMapping("/search")
    List<ItemOut> getItemBySearch(@RequestParam String text,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info(REQUEST_GET_ITEM_BY_QUERY, text);
        return itemService.getItemsBySearch(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    CommentDTO createComment(@RequestHeader(X_SHARER_USER_ID) Long authorId,
                          @PathVariable Long itemId,
                          @Validated(CreateInfo.class) @RequestBody CommentDTO commentDTO) {
        log.info(REQUEST_ADD_COMMENT, authorId);
        return itemService.addComment(authorId, itemId, commentDTO);
    }
}