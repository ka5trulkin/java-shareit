package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemDtoMapper {
    public static Item toItem(long ownerId, ItemDto itemDto) {
        return Item.builder()
                .ownerId(ownerId)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public static Item toItemWhenUpdate(Item item, ItemDto itemDto) {
        final Item updatedItem = Item.builder()
                .id(item.getId())
                .ownerId(item.getOwnerId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
        if (itemDto.getName() != null) {
            updatedItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            updatedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updatedItem.setAvailable(itemDto.getAvailable());
        }
        return updatedItem;
    }
}