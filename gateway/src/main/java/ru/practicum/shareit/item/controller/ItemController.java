package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.utils.CreateInfo;
import ru.practicum.shareit.utils.UpdateInfo;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.item.utils.ItemLogMessage.*;
import static ru.practicum.shareit.utils.PatternsApp.ITEMS_PREFIX;
import static ru.practicum.shareit.utils.PatternsApp.X_SHARER_USER_ID;

@Controller
@RequestMapping(path = ITEMS_PREFIX)
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
	private final ItemClient itemClient;

	@PostMapping
	public ResponseEntity<Object> createItem(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
					   @Validated(CreateInfo.class) @RequestBody ItemDTO itemDTO) {
		log.info(REQUEST_ADD_ITEM, itemDTO.getName());
		return itemClient.addItem(ownerId, itemDTO);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Object> updateItem(@PathVariable Long id,
					   @RequestHeader(X_SHARER_USER_ID) Long ownerId,
					   @Validated(UpdateInfo.class) @RequestBody ItemDTO itemDTO) {
		log.info(REQUEST_UPDATE_ITEM, id);
		return itemClient.updateItem(id, ownerId, itemDTO);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getItemById(@RequestHeader(X_SHARER_USER_ID) Long userId, @PathVariable Long id) {
		log.info(REQUEST_GET_ITEM, id);
		return itemClient.getItemById(id, userId);
	}

	@GetMapping
	public ResponseEntity<Object> getItemsByOwner(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
								  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
								  @RequestParam(defaultValue = "10") @Positive Integer size) {
		log.info(REQUEST_GET_ITEM_LIST);
		return itemClient.getItemsByOwner(ownerId, from, size);
	}

	@GetMapping("/search")
	public ResponseEntity<Object> getItemBySearch(@RequestParam String text,
								  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
								  @RequestParam(defaultValue = "10") @Positive Integer size) {
		log.info(REQUEST_GET_ITEM_BY_QUERY, text);
		return itemClient.getItemsBySearch(text, from, size);
	}

	@PostMapping("/{itemId}/comment")
	public ResponseEntity<Object> createComment(@RequestHeader(X_SHARER_USER_ID) Long authorId,
							 @PathVariable Long itemId,
							 @Validated(CreateInfo.class) @RequestBody CommentDTO commentDTO) {
		log.info(REQUEST_ADD_COMMENT, authorId);
		return itemClient.addComment(authorId, itemId, commentDTO);
	}
}