package ru.practicum.shareit.request.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.request.dto.ItemRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestOutWithItems;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest extends AbstractTest {
    @Mock
    ItemRequestService itemRequestService;
    @InjectMocks
    ItemRequestController controller;

    @Test
    void createItemRequest() {
        final ItemRequestDTO expected = getItemRequestDto();

        when(itemRequestService.addRequest(anyLong(), any())).thenReturn(expected);

        assertThat(controller.createItemRequest(idOne, new ItemRequestCreate(description))).isEqualTo(expected);
    }

    @Test
    void getAllToOwner() {
        final List<ItemRequestOutWithItems> expected = List.of(getItemRequestOutWithItems());

        when(itemRequestService.getAllToOwner(anyLong())).thenReturn(expected);

        final List<ItemRequestOutWithItems> actual = itemRequestService.getAllToOwner(idOne);

        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAll() {
        final List<ItemRequestOutWithItems> expected = List.of(getItemRequestOutWithItems());

        when(itemRequestService.getAll(anyLong(), anyInt(),anyInt())).thenReturn(expected);

        final List<ItemRequestOutWithItems> actual = controller.getAll(idOne, 0, 1);

        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getRequest() {
        final ItemRequestOutWithItems expected = getItemRequestOutWithItems();

        when(itemRequestService.getRequest(anyLong(), anyLong())).thenReturn(expected);

        assertThat(controller.getRequest(idOne, idOne)).isEqualTo(expected);
    }
}