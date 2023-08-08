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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRequestRepositoryIT extends AbstractTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private User user;
    private ItemRequest itemRequest;
    private Long ownerId;

    @BeforeEach
    void beforeEach() {
        user = getUserNoId();
        itemRequest = getItemRequestNoId();
        ownerId = userRepository.save(user).getId();
        itemRequest.setOwner(user);
        itemRequestRepository.save(itemRequest);
    }

    @AfterEach
    void afterEach() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByOwner_Id() {
        final ItemRequest expected = itemRequest;
        final List<ItemRequestDTO> actual = itemRequestRepository.findByOwner_Id(ownerId);

        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual.get(0).getId()).isEqualTo(expected.getId());
        assertThat(actual.get(0).getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.get(0).getCreated()).isAfter(nowTime);
    }

    @Test
    void findByOwner_IdNot() {
        final ItemRequest expected = itemRequest;
        final Long notOwnerId = ownerId + 1;
        final List<ItemRequestDTO> actual = itemRequestRepository.findByOwner_IdNot(notOwnerId, pageable);

        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual.get(0).getId()).isEqualTo(expected.getId());
        assertThat(actual.get(0).getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.get(0).getCreated()).isAfter(nowTime);
    }
}