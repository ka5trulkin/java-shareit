package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.OwnerNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private long id;
    private final Map<Long, Item> itemStorage = new HashMap<>();
    private final Map<Long, List<Long>> ownerItemRelationship = new HashMap<>();

    private void setItemId(Item item) {
        item.setId(++id);
    }

    private void checkItemExists(long id) {
        if (!itemStorage.containsKey(id)) {
            throw new ItemNotFoundException(id);
        }
    }

    private void addToOwnerItemRelationship(Item item) {
        long ownerId = item.getOwner().getId();
        if (!ownerItemRelationship.containsKey(ownerId)) {
            ownerItemRelationship.put(ownerId, new ArrayList<>());
        }
        ownerItemRelationship.get(ownerId).add(item.getId());
    }

    @Override
    public void addItem(Item item) {
        this.setItemId(item);
        itemStorage.put(item.getId(), item);
        this.addToOwnerItemRelationship(item);
    }

    @Override
    public void updateItem(Item item) {
        this.checkItemExists(item.getId());
        this.checkIsOwnerHasItem(item.getId(), item.getOwner().getId());
        itemStorage.put(item.getId(), item);
    }

    @Override
    public Item getItemById(long id) {
        this.checkItemExists(id);
        return itemStorage.get(id);
    }

    @Override
    public Collection<Item> getItemsByOwner(long ownerId) {
        if (!ownerItemRelationship.containsKey(ownerId)) {
            throw new OwnerNotFoundException(ownerId);
        }
        return ownerItemRelationship.get(ownerId).stream()
                .map(itemStorage::get)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> getItemBySearch(String query) {
        return itemStorage.values().stream()
                .filter(Item::isAvailable)
                .filter(item -> item.getName().toLowerCase().contains(query)
                        || item.getDescription().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkIsOwnerHasItem(long id, long ownerId) {
        return ownerItemRelationship.containsKey(ownerId)
                && ownerItemRelationship.get(ownerId).contains(id);
    }
}