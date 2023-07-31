package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.item.dto.item.ItemOut;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest extends AbstractTest {
    @Mock
    private ItemService itemService;
    @InjectMocks
    ItemController controller;

    @Test
    void createItem() {
        final ItemDTO expected = getItemDto();

        when(itemService.addItem(anyLong(), any())).thenReturn(expected);

        assertThat(controller.createItem(idOne, getItemDtoNoId())).isEqualTo(expected);
    }

    @Test
    void updateItem() {
        final ItemDTO expected = getItemDto();

        when(itemService.updateItem(anyLong(), anyLong(), any())).thenReturn(expected);

        assertThat(controller.updateItem(idOne, idOne, getItemDtoNoId())).isEqualTo(expected);
    }

    @Test
    void getItemById() {
        final ItemOut expected = getItemOut();

        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(expected);

        assertThat(controller.getItemById(idOne, idOne)).isEqualTo(expected);
    }

    @Test
    void getItemsByOwner() {
        final List<ItemOut> expected = List.of(getItemOut());

        when(itemService.getItemsByOwner(anyLong(), anyInt(), anyInt())).thenReturn(expected);

        final List<ItemOut> actual = controller.getItemsByOwner(idOne, 0, 1);

        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getItemBySearch() {
        final List<ItemOut> expected = List.of(getItemOut());

        when(itemService.getItemsBySearch(any(), anyInt(), anyInt())).thenReturn(expected);

        final List<ItemOut> actual = controller.getItemBySearch(text, 0, 1);

        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void createComment() {
        final CommentDTO expected = getCommentDto();

        when(itemService.addComment(anyLong(), anyLong(), any())).thenReturn(expected);

        assertThat(controller.createComment(idOne, idOne, getCommentDto())).isEqualTo(expected);
    }
}