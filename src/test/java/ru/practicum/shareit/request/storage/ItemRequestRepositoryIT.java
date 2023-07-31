package ru.practicum.shareit.request.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRequestRepositoryIT extends AbstractTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;
    private ItemRequest itemRequest;
    private Long ownerId;
    private Long anotherUserId;
    private Long itemRequestId;

    @BeforeEach
    void beforeEach() {
        user = getUserNoId();
        itemRequest = getItemRequestNoId();
        ownerId = userRepository.save(user).getId();
        anotherUserId = ownerId + 1;
        itemRequest.setOwner(user);
        itemRequestId = itemRequestRepository.save(itemRequest).getId();
    }

    @AfterEach
    void afterEach() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByOwner_Id() {
        final ItemRequestDTO expectedResult = getItemRequestDto();
        expectedResult.setId(itemRequestId);

        assertThat(itemRequestRepository.findByOwner_Id(ownerId).size()).isEqualTo(1);
        assertThat(itemRequestRepository.findByOwner_Id(ownerId).get(0)).isEqualTo(expectedResult);
    }

    @Test
    void findByOwner_IdNot() {
        final ItemRequestDTO expectedResult = getItemRequestDto();
        expectedResult.setId(itemRequestId);

        assertThat(itemRequestRepository.findByOwner_IdNot(anotherUserId, pageable).size())
                .isEqualTo(1);
        assertThat(itemRequestRepository.findByOwner_IdNot(anotherUserId, pageable).get(0))
                .isEqualTo(expectedResult);
    }
}