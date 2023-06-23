package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

@UtilityClass
public class ItemDtoMapper {
    public Item toItem(long ownerId, ItemDto itemDto) {
        User owner = User.builder().id(ownerId).build();
        return Item.builder()
                .owner(owner)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public Item toItemWhenUpdate(Item item, ItemDto itemDto) {
        User owner = item.getOwner();
        final Item updatedItem = Item.builder()
                .id(item.getId())
                .owner(owner)
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

    public ItemDto fromItem(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .ownerId(item.getOwner().getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }

    public Collection<ItemDto> fromItemCollection(Collection<Item> items) {
        return items.stream()
                .map(ItemDtoMapper::fromItem)
                .collect(Collectors.toList());
    }
}