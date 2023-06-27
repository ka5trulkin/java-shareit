package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
        final BookingView lastBooking = bookingRepository.getLastBooking(id, ownerId, time, BookingView.class)
                .orElse(null);
        final BookingView nextBooking = bookingRepository.getNextBooking(id, ownerId, time, BookingView.class)
                .orElse(null);
        return ItemDTOMapper.fromBookingView(item, lastBooking, nextBooking, comments);
    }

    private ItemWithBookingInfoDTO getItemToUser(ItemDTO item, Collection<CommentDTO> comments) {
        return ItemDTOMapper.fromBookingView(item, comments);
    }

    @Override
    public ItemDTO addItem(Long ownerId, ItemDTO itemDTO) {
        this.checkUserExists(ownerId);
        final Item item = ItemDTOMapper.toItem(ownerId, itemDTO);
        itemRepository.save(item);
        log.info(ITEM_ADDED, item.getId(), item.getName());
        return ItemDTOMapper.fromItem(item);
    }

    @Override
    public CommentDTO addComment(Long authorId, Long itemId, CommentDTO commentDTO) {
        final BookingView booking = bookingRepository.getByBookerIdAndItemId(authorId, itemId, LocalDateTime.now())
                .orElseThrow(() -> new RequestException(BAD_COMMENT_BY_AUTHOR_OR_ITEM));
        Comment comment = CommentDTOMapper.toCommentOnCreate(booking, commentDTO);
        comment = commentRepository.save(comment);
        log.info(COMMENT_ADDED, comment.getId());
        return CommentDTOMapper.fromComment(comment);
    }

    @Override
    public ItemDTO updateItem(Long id, Long ownerId, ItemDTO itemDTO) {
        final Item item = itemRepository.findByIdAndOwnerId(id, ownerId, Item.class)
                .orElseThrow(() -> new ItemNotFoundException(id));
        final Item updatedItem = ItemDTOMapper.toItemWhenUpdate(item, itemDTO);
        itemRepository.save(updatedItem);
        log.info(ITEM_UPDATED, updatedItem.getId());
        return ItemDTOMapper.fromItem(updatedItem);
    }

    @Override
    public ItemDTO getItemById(Long id, Long userId) {
        final ItemDTO item = itemRepository.findById(id, ItemDTO.class)
                .orElseThrow(() -> new ItemNotFoundException(id));
        Collection<CommentView> commentViews = commentRepository.findByItem_Id(item.getId(), CommentView.class);
        Collection<CommentDTO> comments = CommentDTOMapper.fromViewList(commentViews);
        log.info(GET_ITEM, id);
        return isOwner(userId, item) ? this.getItemToOwner(id, userId, item, comments) : this.getItemToUser(item, comments);
    }

    @Override
    public Collection<ItemWithBookingInfoDTO> getItemsByOwner(Long ownerId) {
        final Collection<ItemDTO> items = itemRepository.findAllByOwnerId(ownerId, ItemDTO.class);
        final List<Long> itemsId = items.stream().map(ItemDTO::getId).collect(Collectors.toList());
        final List<BookingView> bookingList = bookingRepository.findByOwnerItems(itemsId, ownerId, BookingView.class);
        final Collection<ItemWithBookingInfoDTO> outList = ItemDTOMapper.fromBookingViewCollection(items, bookingList);
        log.info(GET_ITEM_LIST);
        return outList;
    }

    @Override
    public Collection<ItemDTO> getItemBySearch(String text) {
        if (!text.isBlank()) {
            final Collection<ItemDTO> collection = itemRepository.findByNameOrDescription(text, text, ItemDTO.class);
            log.info(GET_ITEM_BY_QUERY, text);
            return collection;
        }
        return Collections.emptyList();
    }
}