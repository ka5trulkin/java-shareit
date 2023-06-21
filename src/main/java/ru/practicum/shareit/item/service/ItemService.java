package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Item addItem(long ownerId, ItemDto itemDto);

    Item updateItem(long id, long ownerId, ItemDto itemDto);

    Item getItemById(long id);

    Collection<Item> getItemsByOwner(long ownerId);

    Collection<Item> getItemBySearch(String text);
}