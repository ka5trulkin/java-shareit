package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.item.dto.item.ItemInRequest;
import ru.practicum.shareit.item.dto.item.ItemViewWithRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestDTOMapperTest extends AbstractTest {
    @Test
    void toItemRequest() {
        final ItemRequestCreate itemRequestCreate = new ItemRequestCreate(description);
        final ItemRequest actualResult = ItemRequestDTOMapper.toItemRequestOnCreate(idOne, itemRequestCreate);
        actualResult.setCreated(nowTime);

        assertThat(actualResult.getDescription()).isEqualTo(description);
        assertThat(actualResult.getCreated()).isInstanceOf(LocalDateTime.class);
        assertThat(actualResult.getOwner()).isInstanceOf(User.class);
        assertThat(actualResult.getOwner().getId()).isEqualTo(idOne);
    }

    @Test
    void fromItemRequest() {
        final ItemRequest itemRequest = getItemRequest();
        final ItemRequestDTO actualResult = ItemRequestDTOMapper.fromItemRequest(itemRequest);

        assertThat(actualResult.getId()).isEqualTo(itemRequest.getId());
        assertThat(actualResult.getDescription()).isEqualTo(itemRequest.getDescription());
        assertThat(actualResult.getCreated()).isEqualTo(itemRequest.getCreated());
    }

    @Test
    void fromItemViewWithRequest() {
        final ItemViewWithRequest itemView = getItemViewWithRequest();
        final ItemInRequest actualResult = ItemRequestDTOMapper.fromItemViewWithRequest(itemView);

        assertThat(actualResult.getId()).isEqualTo(itemView.getId());
        assertThat(actualResult.getName()).isEqualTo(itemView.getName());
        assertThat(actualResult.getDescription()).isEqualTo(itemView.getDescription());
        assertThat(actualResult.getAvailable()).isEqualTo(itemView.isAvailable());
        assertThat(actualResult.getRequestId()).isEqualTo(itemView.getRequest().getId());
    }

    @Test
    void getOutWithItems() {
        final ItemRequestDTO requestDto = getItemRequestDto();
        final ItemViewWithRequest itemView = getItemViewWithRequest();
        final List<ItemViewWithRequest> itemViewList = List.of(itemView);
        final ItemRequestOutWithItems actualResult = ItemRequestDTOMapper.getOutWithItems(requestDto, itemViewList);

        assertThat(actualResult.getId()).isEqualTo(requestDto.getId());
        assertThat(actualResult.getDescription()).isEqualTo(requestDto.getDescription());
        assertThat(actualResult.getCreated()).isEqualTo(requestDto.getCreated());
        assertThat(actualResult.getItems()).hasSize(1);
        assertThat(actualResult.getItems().get(0).getId()).isEqualTo(itemView.getId());
        assertThat(actualResult.getItems().get(0).getName()).isEqualTo(itemView.getName());
        assertThat(actualResult.getItems().get(0).getDescription()).isEqualTo(itemView.getDescription());
        assertThat(actualResult.getItems().get(0).getAvailable()).isEqualTo(itemView.isAvailable());
        assertThat(actualResult.getItems().get(0).getRequestId()).isEqualTo(itemView.getRequest().getId());
    }

    @Test
    void testGetOutWithItems() {
        final ItemRequest requestDto = getItemRequest();
        final ItemViewWithRequest itemView = getItemViewWithRequest();
        final List<ItemViewWithRequest> itemViewList = List.of(itemView);
        final ItemRequestOutWithItems actualResult = ItemRequestDTOMapper.getOutWithItems(requestDto, itemViewList);

        assertThat(actualResult.getId()).isEqualTo(requestDto.getId());
        assertThat(actualResult.getDescription()).isEqualTo(requestDto.getDescription());
        assertThat(actualResult.getCreated()).isEqualTo(requestDto.getCreated());
        assertThat(actualResult.getItems()).hasSize(1);
        assertThat(actualResult.getItems().get(0).getId()).isEqualTo(itemView.getId());
        assertThat(actualResult.getItems().get(0).getName()).isEqualTo(itemView.getName());
        assertThat(actualResult.getItems().get(0).getDescription()).isEqualTo(itemView.getDescription());
        assertThat(actualResult.getItems().get(0).getAvailable()).isEqualTo(itemView.isAvailable());
        assertThat(actualResult.getItems().get(0).getRequestId()).isEqualTo(itemView.getRequest().getId());
    }

    @Test
    void testGetOutWithItems1() {
        final ItemRequestDTO requestDto = getItemRequestDto();
        final List<ItemRequestDTO> requestDTOList = List.of(requestDto);
        final ItemViewWithRequest itemView = getItemViewWithRequest();
        final List<ItemViewWithRequest> itemViewList = List.of(itemView);
        final List<ItemRequestOutWithItems> actualResult = ItemRequestDTOMapper.getOutWithItems(requestDTOList, itemViewList);

        assertThat(actualResult).hasSize(1);
        assertThat(actualResult.get(0).getId()).isEqualTo(requestDto.getId());
        assertThat(actualResult.get(0).getDescription()).isEqualTo(requestDto.getDescription());
        assertThat(actualResult.get(0).getCreated()).isEqualTo(requestDto.getCreated());
        assertThat(actualResult.get(0).getItems()).hasSize(1);
        assertThat(actualResult.get(0).getItems().get(0).getId()).isEqualTo(itemView.getId());
        assertThat(actualResult.get(0).getItems().get(0).getName()).isEqualTo(itemView.getName());
        assertThat(actualResult.get(0).getItems().get(0).getDescription()).isEqualTo(itemView.getDescription());
        assertThat(actualResult.get(0).getItems().get(0).getAvailable()).isEqualTo(itemView.isAvailable());
        assertThat(actualResult.get(0).getItems().get(0).getRequestId()).isEqualTo(itemView.getRequest().getId());
    }
}