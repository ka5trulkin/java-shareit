package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemDAO {
    void addItem(Item item);

    void updateItem(Item item);

    Item getItemById(long id);

    Collection<Item> getItemsByOwner(long ownerId);

    Collection<Item> getItemBySearch(String query);

    boolean checkIsOwnerHasItem(long ownerId, long id);
}