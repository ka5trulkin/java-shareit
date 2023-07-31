package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.item.dto.comment.CommentView;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryIT extends AbstractTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    CommentRepository commentRepository;
    private Item item;
    private User user;
    private ItemRequest itemRequest;
    private Comment comment;
    private Long itemId;

    @BeforeEach
    void setUp() {
        user = getUserNoId();
        itemRequest = getItemRequestNoId();
        item = getItemNoId();
        comment = getCommentNoId();
        userRepository.save(user);
        itemRequest.setOwner(user);
        itemRequestRepository.save(itemRequest);
        item.setOwner(user);
        item.setRequest(itemRequest);
        itemId = itemRepository.save(item).getId();
        comment.setAuthor(user);
        comment.setItem(item);
        commentRepository.save(comment);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByItem_Id() {
        final CommentView expectedResult = getCommentView(comment);
        final List<CommentView> actualResult = commentRepository.findByItem_Id(itemId);

        assertThat(actualResult).hasSize(1);
        assertThat(actualResult.get(0).getId()).isEqualTo(expectedResult.getId());
        assertThat(actualResult.get(0).getText()).isEqualTo(expectedResult.getText());
        assertThat(actualResult.get(0).getCreated()).isEqualTo(expectedResult.getCreated());
        assertThat(actualResult.get(0).getAuthor().getId()).isEqualTo(expectedResult.getAuthor().getId().intValue());
        assertThat(actualResult.get(0).getAuthor().getName()).isEqualTo(expectedResult.getAuthor().getName());
        assertThat(actualResult.get(0).getItem().getId()).isEqualTo(expectedResult.getItem().getId().intValue());
        assertThat(actualResult.get(0).getItem().getName()).isEqualTo(expectedResult.getItem().getName());
    }

    @Test
    void findByItem_IdIn() {
        final CommentView expectedResult = getCommentView(comment);
        final List<CommentView> actualResult = commentRepository.findByItem_IdIn(List.of(itemId));

        assertThat(actualResult).hasSize(1);
        assertThat(actualResult.get(0).getId()).isEqualTo(expectedResult.getId());
        assertThat(actualResult.get(0).getText()).isEqualTo(expectedResult.getText());
        assertThat(actualResult.get(0).getCreated()).isEqualTo(expectedResult.getCreated());
        assertThat(actualResult.get(0).getAuthor().getId()).isEqualTo(expectedResult.getAuthor().getId().intValue());
        assertThat(actualResult.get(0).getAuthor().getName()).isEqualTo(expectedResult.getAuthor().getName());
        assertThat(actualResult.get(0).getItem().getId()).isEqualTo(expectedResult.getItem().getId().intValue());
        assertThat(actualResult.get(0).getItem().getName()).isEqualTo(expectedResult.getItem().getName());
    }
}