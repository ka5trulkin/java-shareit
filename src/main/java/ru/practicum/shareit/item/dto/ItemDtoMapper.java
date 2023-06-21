package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemDtoMapper {
    public static Item toItem(long ownerId, ItemDto itemDto) {
        return Item.builder().
                ownerId(ownerId).
                name(itemDto.getName()).
                description(itemDto.getDescription()).
                available(itemDto.getAvailable()).
                build();
    }
}