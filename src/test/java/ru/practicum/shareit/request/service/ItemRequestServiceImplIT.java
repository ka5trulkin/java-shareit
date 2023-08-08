package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.request.dto.ItemRequestOutWithItems;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItemRequestServiceImplIT extends AbstractTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private ItemRequestService itemRequestService;
    private User user;
    private ItemRequest itemRequest;
    private Long ownerId;
    private Long itemRequestId;

    @BeforeEach
    void beforeEach() {
        user = getUserNoId();
        itemRequest = getItemRequestNoId();
        ownerId = userRepository.save(user).getId();
        itemRequest.setOwner(user);
        itemRequestId = itemRequestRepository.save(itemRequest).getId();
    }

    @AfterEach
    void afterEach() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void getRequest() {
        final ItemRequestOutWithItems actual = itemRequestService.getRequest(ownerId, itemRequestId);

        assertThat(actual.getId()).isEqualTo(itemRequestId);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getCreated()).isAfter(nowTime);
    }
}