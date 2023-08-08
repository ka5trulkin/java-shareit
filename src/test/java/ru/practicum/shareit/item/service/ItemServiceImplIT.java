package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.item.ItemOut;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItemServiceImplIT extends AbstractTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemService itemService;
    private Item item;
    private User user;
    private ItemRequest itemRequest;
    private Comment comment;
    private Booking booking;
    private Long userId;
    private Long itemId;

    @BeforeEach
    void setUp() {
        user = getUserNoId();
        itemRequest = getItemRequestNoId();
        item = getItemNoId();
        comment = getCommentNoId();
        userId = userRepository.save(user).getId();
        itemRequest.setOwner(user);
        itemRequestRepository.save(itemRequest);
        item.setOwner(user);
        item.setRequest(itemRequest);
        itemId = itemRepository.save(item).getId();
        booking = getBookingNoId();
        booking.setBooker(user);
        booking.setItem(item);
        bookingRepository.save(booking);
        comment.setAuthor(user);
        comment.setItem(item);
        commentRepository.save(comment);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void addComment() {
        final Long expectedCommentId = comment.getId() + 1;
        final String expectedName = user.getName();
        final CommentDTO commentDTO = CommentDTO.builder()
                .text(text)
                .authorName(expectedName)
                .build();

        final CommentDTO actual = itemService.addComment(userId, itemId, commentDTO);

        assertThat(actual.getId()).isEqualTo(expectedCommentId);
        assertThat(actual.getText()).isEqualTo(text);
        assertThat(actual.getAuthorName()).isEqualTo(expectedName);
        assertThat(actual.getCreated()).isAfter(nowTime);
    }

    @Test
    void getItemById() {
        final ItemOut actual = itemService.getItemById(itemId, userId);

        assertThat(actual.getId()).isEqualTo(itemId);
        assertThat(actual.getOwnerId()).isEqualTo(userId);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getAvailable()).isEqualTo(true);
        assertThat(actual.getRequestId()).isEqualTo(itemRequest.getId());
    }
}