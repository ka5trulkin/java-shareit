package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
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
        try {
            userRepository.getUser(item.getOwnerId());
        } catch (UserNotFoundExistException e) {
            throw new ItemNotFoundException();
        }
    }

    private void checkOwnerOnUpdate(long id, long ownerId) {
        try {
            userRepository.getUser(ownerId);
        } catch (UserNotFoundExistException e) {
            throw new ItemNotFoundException();
        }
        if (!itemRepository.checkIsOwnerHasItem(id, ownerId)) {
            throw new ItemNotFoundException();
        }
    }

    @Override
    public Item addItem(long ownerId, ItemDto itemDto) {
        Item item = ItemDtoMapper.toItem(ownerId, itemDto);
        this.checkOwnerOnCreate(item);
        itemRepository.addItem(item);
        log.info(ITEM_ADDED.message(), item.getId(), item.getName());
        return item;
    }

    @Override
    public Item updateItem(long id, long ownerId, ItemDto itemDto) {
        this.checkOwnerOnUpdate(id, ownerId);
        final Item item = itemRepository.getItemById(id);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemRepository.updateItem(item);
        log.info(ITEM_UPDATED.message(), item.getId());
        return item;
    }

    @Override
    public Item getItemById(long id) {
        Item item = itemRepository.getItemById(id);
        log.info(GET_ITEM.message(), id);
        return item;
    }

    @Override
    public Collection<Item> getItemsByOwner(long ownerId) {
        Collection<Item> collection = itemRepository.getItemsByOwner(ownerId);
        log.info(GET_ITEM_LIST.message());
        return collection;
    }

    @Override
    public Collection<Item> getItemBySearch(String text) {
        String query = text.toLowerCase();
        if (!query.isBlank()) {
            Collection<Item> collection = itemRepository.getItemBySearch(query);
            log.info(GET_ITEM_BY_QUERY.message(), text);
            return collection;
        }
        return Collections.emptyList();
    }
}