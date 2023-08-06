package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.item.dto.item.ItemDTOMapper;
import ru.practicum.shareit.item.dto.item.ItemViewWithRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRepositoryIT extends AbstractTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    private Item item;
    private User user;
    private ItemRequest itemRequest;
    private Long userId;
    private Long itemId;
    private Long itemRequestId;

    @BeforeEach
    void beforeEach() {
        user = getUserNoId();
        itemRequest = getItemRequestNoId();
        item = getItemNoId();
        userId = userRepository.save(user).getId();
        itemRequest.setOwner(user);
        itemRequestId = itemRequestRepository.save(itemRequest).getId();
        item.setOwner(user);
        item.setRequest(itemRequest);
        itemId = itemRepository.save(item).getId();
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByIdAndOwnerId() {
        final Item expectedResult = getItemNoId();
        expectedResult.setId(itemId);
        expectedResult.setOwner(user);
        expectedResult.setRequest(itemRequest);

        assertThat(itemRepository.findByIdAndOwnerId(itemId, userId).orElseThrow()).isEqualTo(expectedResult);
    }

    @Test
    void findByOwnerId() {
        final ItemDTO itemDTO = ItemDTOMapper.fromItem(item);
        final List<ItemDTO> expectedResult = List.of(itemDTO);

        assertThat(itemRepository.findByOwnerId(userId, pageable)).isEqualTo(expectedResult);
    }

    @Test
    void findByNameOrDescription_whenSearchByName() {
        final ItemDTO itemDto = ItemDTOMapper.fromItem(item);
        String text = getSubstringText(itemDto.getName());
        final List<ItemDTO> expectedResult = List.of(itemDto);
        final List<ItemDTO> actual = itemRepository.findByNameOrDescription(text, pageable);

        assertThat(actual).isEqualTo(expectedResult);
    }

    @Test
    void findByNameOrDescription_whenSearchByDescription() {
        final ItemDTO itemDto = ItemDTOMapper.fromItem(item);
        String text = getSubstringText(itemDto.getDescription());
        final List<ItemDTO> expectedResult = List.of(itemDto);
        final List<ItemDTO> actual = itemRepository.findByNameOrDescription(text, pageable);

        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual).isEqualTo(expectedResult);
    }



    @Test
    void findWithRequestsByOwnerId() {
        final ItemViewWithRequest itemView = getItemViewWithRequest();
        itemView.setId(itemId);
        itemView.getRequest().setId(itemRequestId);
        final List<ItemViewWithRequest> expectedResult = List.of(itemView);
        final List<ItemViewWithRequest> actualResult = itemRepository.findWithRequestsByOwnerId(userId);

        assertThat(actualResult).hasSize(1);
        assertThat(actualResult.get(0).getId()).isEqualTo(expectedResult.get(0).getId());
        assertThat(actualResult.get(0).getName()).isEqualTo(expectedResult.get(0).getName());
        assertThat(actualResult.get(0).getDescription()).isEqualTo(expectedResult.get(0).getDescription());
        assertThat(actualResult.get(0).isAvailable()).isEqualTo(expectedResult.get(0).isAvailable());
        assertThat(actualResult.get(0).getRequest().getId()).isEqualTo(expectedResult.get(0).getRequest().getId());
    }

    @Test
    void findByRequestIdIn() {
        final ItemViewWithRequest itemView = getItemViewWithRequest();
        itemView.setId(itemId);
        itemView.getRequest().setId(itemRequestId);
        final List<ItemViewWithRequest> expectedResult = List.of(itemView);
        final List<ItemViewWithRequest> actualResult = itemRepository.findByRequestIdIn(List.of(itemRequestId));

        assertThat(actualResult).hasSize(1);
        assertThat(actualResult.get(0).getId()).isEqualTo(expectedResult.get(0).getId());
        assertThat(actualResult.get(0).getName()).isEqualTo(expectedResult.get(0).getName());
        assertThat(actualResult.get(0).getDescription()).isEqualTo(expectedResult.get(0).getDescription());
        assertThat(actualResult.get(0).isAvailable()).isEqualTo(expectedResult.get(0).isAvailable());
        assertThat(actualResult.get(0).getRequest().getId()).isEqualTo(expectedResult.get(0).getRequest().getId());
    }

    @Test
    void findByRequestId() {
        final ItemViewWithRequest itemView = getItemViewWithRequest();
        itemView.setId(itemId);
        itemView.getRequest().setId(itemRequestId);
        final List<ItemViewWithRequest> expectedResult = List.of(itemView);
        final List<ItemViewWithRequest> actualResult = itemRepository.findByRequestId(itemRequestId);

        assertThat(actualResult).hasSize(1);
        assertThat(actualResult.get(0).getId()).isEqualTo(expectedResult.get(0).getId());
        assertThat(actualResult.get(0).getName()).isEqualTo(expectedResult.get(0).getName());
        assertThat(actualResult.get(0).getDescription()).isEqualTo(expectedResult.get(0).getDescription());
        assertThat(actualResult.get(0).isAvailable()).isEqualTo(expectedResult.get(0).isAvailable());
        assertThat(actualResult.get(0).getRequest().getId()).isEqualTo(expectedResult.get(0).getRequest().getId());
    }
}