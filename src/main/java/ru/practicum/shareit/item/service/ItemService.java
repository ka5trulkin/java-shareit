package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;

import java.util.Collection;

@Transactional(readOnly = true)
public interface ItemService {
    @Transactional
    ItemDTO addItem(Long ownerId, ItemDTO itemDTO);

    @Transactional
    CommentDTO addComment(Long authorId, Long itemId, CommentDTO commentDTO);

    @Transactional
    ItemDTO updateItem(Long id, Long ownerId, ItemDTO itemDTO);

    ItemDTO getItemById(Long id, Long userId);

    <T extends ItemDTO> Collection<T> getItemsByOwner(Long ownerId);

    Collection<ItemDTO> getItemBySearch(String text);
}