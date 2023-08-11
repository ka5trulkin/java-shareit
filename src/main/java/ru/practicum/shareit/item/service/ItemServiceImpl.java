package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.base.RequestException;
import ru.practicum.shareit.exception.item.ItemNotFoundException;
import ru.practicum.shareit.exception.item.OwnerNotFoundException;
import ru.practicum.shareit.exception.item_request.ItemRequestNotFoundException;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTOMapper;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.item.dto.item.ItemDTOMapper;
import ru.practicum.shareit.item.dto.comment.CommentView;
import ru.practicum.shareit.item.dto.item.ItemOut;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.utils.PageApp;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.exception.base.ExceptionMessage.BAD_COMMENT_BY_AUTHOR_OR_ITEM;
import static ru.practicum.shareit.item.ItemLogMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new OwnerNotFoundException(userId);
        }
    }

    private void checkRequestExists(ItemDTO itemDTO) {
        if ((itemDTO.getRequestId() != null) && (!itemRequestRepository.existsById(itemDTO.getRequestId()))) {
            throw new ItemRequestNotFoundException(itemDTO.getRequestId());
        }
    }

    private boolean isOwner(Long userId, ItemDTO item) {
        return Objects.equals(userId, item.getOwnerId());
    }

    private ItemOut getItemToOwner(Long id, Long ownerId, ItemDTO item, List<CommentDTO> comments) {
        final LocalDateTime time = LocalDateTime.now();
        final BookingView lastBooking = bookingRepository.getLastBooking(id, ownerId, time)
                .orElse(null);
        final BookingView nextBooking = bookingRepository.getNextBooking(id, ownerId, time)
                .orElse(null);
        return ItemDTOMapper.fromBookingView(item, lastBooking, nextBooking, comments);
    }

    private ItemOut getItemToUser(ItemDTO item, List<CommentDTO> comments) {
        return ItemDTOMapper.fromBookingView(item, comments);
    }

    private List<ItemOut> getOutDTOList(Collection<ItemDTO> items) {
        final List<Long> itemsId = items.stream().map(ItemDTO::getId).collect(Collectors.toList());
        final List<BookingView> bookings = bookingRepository.findByItemIdIn(itemsId);
        final List<CommentView> comments = commentRepository.findByItem_IdIn(itemsId);
        return ItemDTOMapper.fromBookingViewCollection(items, bookings, comments);
    }

    @Override
    @Transactional
    public ItemDTO addItem(Long ownerId, ItemDTO itemDTO) {
        this.checkUserExists(ownerId);
        this.checkRequestExists(itemDTO);
        Item item = ItemDTOMapper.toItem(ownerId, itemDTO);
        item = itemRepository.save(item);
        log.info(ITEM_ADDED, item.getId(), item.getName());
        return ItemDTOMapper.fromItem(item);
    }

    @Override
    @Transactional
    public CommentDTO addComment(Long authorId, Long itemId, CommentDTO commentDTO) {
        final BookingView booking = bookingRepository.getByBookerIdAndItemId(authorId, itemId, LocalDateTime.now())
                .orElseThrow(() -> new RequestException(BAD_COMMENT_BY_AUTHOR_OR_ITEM));
        Comment comment = CommentDTOMapper.toCommentOnCreate(booking, commentDTO);
        comment = commentRepository.save(comment);
        log.info(COMMENT_ADDED, comment.getId());
        return CommentDTOMapper.fromComment(comment);
    }

    @Override
    @Transactional
    public ItemDTO updateItem(Long id, Long ownerId, ItemDTO itemDTO) {
        final Item item = itemRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new ItemNotFoundException(id));
        Item updatedItem = ItemDTOMapper.toItemWhenUpdate(item, itemDTO);
        updatedItem = itemRepository.save(updatedItem);
        log.info(ITEM_UPDATED, updatedItem.getId());
        return ItemDTOMapper.fromItem(updatedItem);
    }

    @Override
    public ItemOut getItemById(Long id, Long userId) {
        final Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        final ItemDTO itemDTO = ItemDTOMapper.fromItem(item);
        final List<CommentView> commentViews = commentRepository.findByItem_Id(itemDTO.getId());
        final List<CommentDTO> comments = CommentDTOMapper.fromViewList(commentViews);
        log.info(GET_ITEM, id);
        return isOwner(userId, itemDTO) ? this.getItemToOwner(id, userId, itemDTO, comments) : this.getItemToUser(itemDTO, comments);
    }

    @Override
    public List<ItemOut> getItemsByOwner(Long ownerId, Integer from, Integer size) {
        final List<ItemDTO> items = itemRepository.findByOwnerId(ownerId, PageApp.ofStartingIndex(from, size));
        log.info(GET_ITEM_LIST);
        return this.getOutDTOList(items);
    }

    @Override
    public List<ItemOut> getItemsBySearch(String text, Integer from, Integer size) {
        if (text == null || text.isBlank()) {
            log.info(GET_ITEM_LIST_BY_QUERY, text);
            return Collections.emptyList();
        }
        final List<ItemDTO> items = itemRepository.findByNameOrDescription(text, PageApp.ofStartingIndex(from, size));
        log.info(GET_ITEM_LIST_BY_QUERY, text);
        return this.getOutDTOList(items);
    }
}