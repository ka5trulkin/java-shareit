package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemWithBookingInfoDTO;

import java.util.Collection;

public interface ItemService {
    ItemDTO addItem(Long ownerId, ItemDTO itemDTO);

    CommentDTO addComment(Long authorId, Long itemId, CommentDTO commentDTO);

    ItemDTO updateItem(Long id, Long ownerId, ItemDTO itemDTO);

    ItemDTO getItemById(Long id, Long userId);

    Collection<ItemWithBookingInfoDTO> getItemsByOwner(Long ownerId);

    Collection<ItemWithBookingInfoDTO> getItemsBySearch(String text);
}