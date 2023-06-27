package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.item.ItemNotFoundException;
import ru.practicum.shareit.exception.item.OwnerNotFoundException;
import ru.practicum.shareit.exception.user.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemDTOMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemDAO;
import ru.practicum.shareit.user.storage.UserDAO;

import java.util.Collection;
import java.util.Collections;

import static ru.practicum.shareit.item.ItemLogMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceInMemory implements ItemService {
    private final ItemDAO itemRepository;
    private final UserDAO userRepository;

    private void checkOwnerOnCreate(Item item) {
        Long ownerId = item.getOwner().getId();
        try {
            userRepository.getUser(ownerId);
        } catch (UserNotFoundException e) {
            throw new OwnerNotFoundException(ownerId);
        }
    }

    private void checkOwnerOnUpdate(Long id, Long ownerId) {
        try {
            userRepository.getUser(ownerId);
        } catch (UserNotFoundException e) {
            throw new OwnerNotFoundException(ownerId);
        }
        if (!itemRepository.checkIsOwnerHasItem(id, ownerId)) {
            throw new ItemNotFoundException(id);
        }
    }

    @Override
    public ItemDTO addItem(Long ownerId, ItemDTO itemDTO) {
        final Item item = ItemDTOMapper.toItem(ownerId, itemDTO);
        this.checkOwnerOnCreate(item);
        itemRepository.addItem(item);
        log.info(ITEM_ADDED, item.getId(), item.getName());
        return ItemDTOMapper.fromItem(item);
    }

    @Override
    public CommentDTO addComment(Long authorId, Long itemId, CommentDTO commentDTO) {
        return null;
    }

    @Override
    public ItemDTO updateItem(Long id, Long ownerId, ItemDTO itemDTO) {
        this.checkOwnerOnUpdate(id, ownerId);
        final Item item = itemRepository.getItemById(id);
        final Item updatedItem = ItemDTOMapper.toItemWhenUpdate(item, itemDTO);
        itemRepository.updateItem(updatedItem);
        log.info(ITEM_UPDATED, updatedItem.getId());
        return ItemDTOMapper.fromItem(updatedItem);
    }

    @Override
    public ItemDTO getItemById(Long id, Long userId) {
        final Item item = itemRepository.getItemById(id);
        log.info(GET_ITEM, id);
        return ItemDTOMapper.fromItem(item);
    }

    @Override
    public Collection<ItemDTO> getItemsByOwner(Long ownerId) {
        final Collection<Item> collection = itemRepository.getItemsByOwner(ownerId);
        log.info(GET_ITEM_LIST);
        return ItemDTOMapper.fromItemCollection(collection);
    }

    @Override
    public Collection<ItemDTO> getItemBySearch(String text) {
        String query = text.toLowerCase();
        if (!query.isBlank()) {
            final Collection<Item> collection = itemRepository.getItemBySearch(query);
            log.info(GET_ITEM_BY_QUERY, text);
            return ItemDTOMapper.fromItemCollection(collection);
        }
        return Collections.emptyList();
    }
}