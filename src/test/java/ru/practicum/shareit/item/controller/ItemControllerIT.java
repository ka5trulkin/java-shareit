package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.item.dto.item.ItemOut;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.PatternsApp.X_SHARER_USER_ID;

@WebMvcTest
class ItemControllerIT extends AbstractTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private ItemRequestService requestService;
    private final String urlPath = "/items";
    private final String urlPathId = String.join("/", urlPath, "{id}");
    private final String urlPathSearch = String.join("/", urlPath, "search");
    private final String urlPathIdComment = String.join("/", urlPath, "{itemId}/comment");

    @SneakyThrows
    @Test
    void createItem() {
        final ItemDTO itemDto = getItemDto();

        when(itemService.addItem(anyLong(), any())).thenReturn(itemDto);

        String result = mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(itemDto));
    }

    @SneakyThrows
    @Test
    void createItem_whenNameIsNull_thenIsBadRequest() {
        final ItemDTO itemDto = getItemDto();
        itemDto.setName(null);

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).addItem(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createItem_whenNameIsBlank_thenIsBadRequest() {
        final ItemDTO itemDto = getItemDto();
        itemDto.setName(" ");

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).addItem(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createItem_whenDescriptionIsNull_thenIsBadRequest() {
        final ItemDTO itemDto = getItemDto();
        itemDto.setDescription(null);

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).addItem(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createItem_whenDescriptionIsBlank_thenIsBadRequest() {
        final ItemDTO itemDto = getItemDto();
        itemDto.setDescription(" ");

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).addItem(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createItem_whenAvailableIsNull_thenIsBadRequest() {
        final ItemDTO itemDto = getItemDto();
        itemDto.setAvailable(null);

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).addItem(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void updateItem() {
        final ItemDTO itemDto = getItemDto();

        when(itemService.updateItem(anyLong(), anyLong(), any())).thenReturn(itemDto);

        String result = mockMvc.perform(patch(urlPathId, idOne)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(itemDto));
    }

    @SneakyThrows
    @Test
    void updateItem_whenNameAndDescriptionAndAvailableIsNull_thenIsServerError() {
        mockMvc.perform(patch(urlPathId, idOne)
                        .header(X_SHARER_USER_ID, idOne))
                .andExpect(status().is5xxServerError());

        verify(itemService, never()).updateItem(anyLong(), anyLong(), any());
    }


    @SneakyThrows
    @Test
    void getItemById() {
        final ItemOut itemOut = getItemOut();

        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemOut);

        String result = mockMvc.perform(get(urlPathId, idOne)
                        .header(X_SHARER_USER_ID, idOne))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(itemOut));
    }

    @SneakyThrows
    @Test
    void getItemsByOwner() {
        final List<ItemOut> itemOut = List.of(getItemOut());

        when(itemService.getItemsByOwner(anyLong(), anyInt(), anyInt())).thenReturn(itemOut);

        String result = mockMvc.perform(get(urlPath)
                        .header(X_SHARER_USER_ID, idOne))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(itemOut));
    }

    @SneakyThrows
    @Test
    void getItemsByOwner_whenFromIsLessThenZero_thenIsServerError() {
        mockMvc.perform(get(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("from", "-1"))
                .andExpect(status().is5xxServerError());

        verify(itemService, never()).getItemsByOwner(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getItemsByOwner_whenSizeIsLessThenZero_thenIsServerError() {
        mockMvc.perform(get(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("size", "-1"))
                .andExpect(status().is5xxServerError());

        verify(itemService, never()).getItemsByOwner(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getItemsByOwner_whenSizeIsMoreThenFifty_thenIsServerError() {
        mockMvc.perform(get(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("size", "51"))
                .andExpect(status().is5xxServerError());

        verify(itemService, never()).getItemsByOwner(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getItemBySearch() {
        final List<ItemOut> itemOut = List.of(getItemOut());

        when(itemService.getItemsBySearch(any(), anyInt(), anyInt())).thenReturn(itemOut);

        String result = mockMvc.perform(get(urlPathSearch)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("text", text))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(itemOut));
    }

    @SneakyThrows
    @Test
    void getItemBySearch_whenFromIsLessThenZero_thenIsServerError() {
        mockMvc.perform(get(urlPathSearch)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("text", text)
                        .param("from", "-1"))
                .andExpect(status().is5xxServerError());

        verify(itemService, never()).getItemsBySearch(any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getItemBySearch_whenSizeIsLessThenZero_thenIsServerError() {
        mockMvc.perform(get(urlPathSearch)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("text", text)
                        .param("size", "-1"))
                .andExpect(status().is5xxServerError());

        verify(itemService, never()).getItemsBySearch(any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getItemBySearch_whenSizeIsMoreThenFifty_thenIsServerError() {
        mockMvc.perform(get(urlPathSearch)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("text", text)
                        .param("size", "51"))
                .andExpect(status().is5xxServerError());

        verify(itemService, never()).getItemsBySearch(any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void createComment() {
        final CommentDTO commentDto = getCommentDto();

        when(itemService.addComment(anyLong(), anyLong(), any())).thenReturn(commentDto);

        String result = mockMvc.perform(post(urlPathIdComment, idOne)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(commentDto));
    }

    @SneakyThrows
    @Test
    void createComment_whenTextIsNull_thenIsBadRequest() {
        final CommentDTO commentDto = getCommentDto();
        commentDto.setText(null);

        mockMvc.perform(post(urlPathIdComment, idOne)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).addComment(anyLong(), anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createComment_whenTextIsBlank_thenIsBadRequest() {
        final CommentDTO commentDto = getCommentDto();
        commentDto.setText(" ");

        mockMvc.perform(post(urlPathIdComment, idOne)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).addComment(anyLong(), anyLong(), any());
    }
}