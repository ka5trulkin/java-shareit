package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addItem(long ownerId, ItemDto itemDto);

    ItemDto updateItem(long id, long ownerId, ItemDto itemDto);

    ItemDto getItemById(long id);

    Collection<ItemDto> getItemsByOwner(long ownerId);

    Collection<ItemDto> getItemBySearch(String text);
}