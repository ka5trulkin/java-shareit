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
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

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

    private void checkUserExists(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new OwnerNotFoundException(userId));
    }

    private boolean isOwner(Long userId, ItemDTO item) {
        return Objects.equals(userId, item.getOwnerId());
    }

    private ItemWithBookingInfoDTO getItemToOwner(Long id, Long ownerId, ItemDTO item, Collection<CommentDTO> comments) {
        final LocalDateTime time = LocalDateTime.now();
        final BookingView lastBooking = bookingRepository.getLastBooking(id, ownerId, time)
                .orElse(null);
        final BookingView nextBooking = bookingRepository.getNextBooking(id, ownerId, time)
                .orElse(null);
        return ItemDTOMapper.fromBookingView(item, lastBooking, nextBooking, comments);
    }

    private ItemWithBookingInfoDTO getItemToUser(ItemDTO item, Collection<CommentDTO> comments) {
        return ItemDTOMapper.fromBookingView(item, comments);
    }

    private Collection<ItemWithBookingInfoDTO> getOutDTOList(Collection<ItemDTO> items) {
        final List<Long> itemsId = items.stream().map(ItemDTO::getId).collect(Collectors.toList());
        final List<BookingView> bookings = bookingRepository.findByItem_IdIn(itemsId);
        final List<CommentView> comments = commentRepository.findByItem_IdIn(itemsId);
        return ItemDTOMapper.fromBookingViewCollection(items, bookings, comments);
    }

    @Override
    @Transactional
    public ItemDTO addItem(Long ownerId, ItemDTO itemDTO) {
        this.checkUserExists(ownerId);
        final Item item = ItemDTOMapper.toItem(ownerId, itemDTO);
        itemRepository.save(item);
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
        final Item updatedItem = ItemDTOMapper.toItemWhenUpdate(item, itemDTO);
        itemRepository.save(updatedItem);
        log.info(ITEM_UPDATED, updatedItem.getId());
        return ItemDTOMapper.fromItem(updatedItem);
    }

    @Override
    public ItemDTO getItemById(Long id, Long userId) {
        final Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        final ItemDTO itemDTO = ItemDTOMapper.fromItem(item);
        Collection<CommentView> commentViews = commentRepository.findByItem_Id(itemDTO.getId());
        Collection<CommentDTO> comments = CommentDTOMapper.fromViewList(commentViews);
        log.info(GET_ITEM, id);
        return isOwner(userId, itemDTO) ? this.getItemToOwner(id, userId, itemDTO, comments) : this.getItemToUser(itemDTO, comments);
    }

    @Override
    public Collection<ItemWithBookingInfoDTO> getItemsByOwner(Long ownerId) {
        final Collection<ItemDTO> items = itemRepository.findAllByOwnerId(ownerId);
        final Collection<ItemWithBookingInfoDTO> outList = this.getOutDTOList(items);
        log.info(GET_ITEM_LIST);
        return outList;
    }

    @Override
    public Collection<ItemWithBookingInfoDTO> getItemsBySearch(String text) {
        if (!text.isBlank()) {
            final Collection<ItemDTO> items = itemRepository.findByNameOrDescription(text, text);
            final Collection<ItemWithBookingInfoDTO> outList = this.getOutDTOList(items);
            log.info(GET_ITEM_BY_QUERY, text);
            return outList;
        }
        return Collections.emptyList();
    }
}