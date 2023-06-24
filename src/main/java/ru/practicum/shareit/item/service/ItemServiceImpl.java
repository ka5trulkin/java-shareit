package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.OwnerNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundExistException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;

import static ru.practicum.shareit.item.ItemLogMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private void checkOwnerOnCreate(Item item) {
        long ownerId = item.getOwner().getId();
        try {
            userRepository.getUser(ownerId);
        } catch (UserNotFoundExistException e) {
            throw new OwnerNotFoundException(ownerId);
        }
    }

    private void checkOwnerOnUpdate(long id, long ownerId) {
        try {
            userRepository.getUser(ownerId);
        } catch (UserNotFoundExistException e) {
            throw new OwnerNotFoundException(ownerId);
        }
        if (!itemRepository.checkIsOwnerHasItem(id, ownerId)) {
            throw new ItemNotFoundException(id);
        }
    }

    @Override
    public ItemDto addItem(long ownerId, ItemDto itemDto) {
        Item item = ItemDtoMapper.toItem(ownerId, itemDto);
        this.checkOwnerOnCreate(item);
        itemRepository.addItem(item);
        log.info(ITEM_ADDED, item.getId(), item.getName());
        return ItemDtoMapper.fromItem(item);
    }

    @Override
    public ItemDto updateItem(long id, long ownerId, ItemDto itemDto) {
        this.checkOwnerOnUpdate(id, ownerId);
        final Item item = itemRepository.getItemById(id);
        final Item updatedItem = ItemDtoMapper.toItemWhenUpdate(item, itemDto);
        itemRepository.updateItem(updatedItem);
        log.info(ITEM_UPDATED, updatedItem.getId());
        return ItemDtoMapper.fromItem(updatedItem);
    }

    @Override
    public ItemDto getItemById(long id) {
        Item item = itemRepository.getItemById(id);
        log.info(GET_ITEM, id);
        return ItemDtoMapper.fromItem(item);
    }

    @Override
    public Collection<ItemDto> getItemsByOwner(long ownerId) {
        Collection<Item> collection = itemRepository.getItemsByOwner(ownerId);
        log.info(GET_ITEM_LIST);
        return ItemDtoMapper.fromItemCollection(collection);
    }

    @Override
    public Collection<ItemDto> getItemBySearch(String text) {
        String query = text.toLowerCase();
        if (!query.isBlank()) {
            Collection<Item> collection = itemRepository.getItemBySearch(query);
            log.info(GET_ITEM_BY_QUERY, text);
            return ItemDtoMapper.fromItemCollection(collection);
        }
        return Collections.emptyList();
    }
}