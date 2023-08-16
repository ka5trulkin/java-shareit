package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.item.dto.item.ItemOut;

import java.util.List;

public interface ItemService {
    ItemDTO addItem(Long ownerId, ItemDTO itemDTO);

    CommentDTO addComment(Long authorId, Long itemId, CommentDTO commentDTO);

    ItemDTO updateItem(Long id, Long ownerId, ItemDTO itemDTO);

    ItemOut getItemById(Long id, Long userId);

    List<ItemOut> getItemsByOwner(Long ownerId, Integer from, Integer size);

    List<ItemOut> getItemsBySearch(String text, Integer from, Integer size);
}