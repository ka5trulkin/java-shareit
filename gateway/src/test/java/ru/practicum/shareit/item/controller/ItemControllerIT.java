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
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.user.client.UserClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.PatternsApp.ITEMS_PREFIX;
import static ru.practicum.shareit.utils.PatternsApp.X_SHARER_USER_ID;

@WebMvcTest
class ItemControllerIT extends AbstractTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemRequestClient itemRequestClient;
    @MockBean
    private ItemClient itemClient;
    @MockBean
    private BookingClient bookingClient;
    @MockBean
    private UserClient userClient;
    private final String urlPathIdComment = String.join("/", ITEMS_PREFIX, "{itemId}/comment");

    @SneakyThrows
    @Test
    void createItem_whenNameIsBlank_thenIsBadRequest() {
        final ItemDTO itemDto = getItemDto();
        itemDto.setName(" ");

        mockMvc.perform(post(ITEMS_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).addItem(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createItem_whenDescriptionIsBlank_thenIsBadRequest() {
        final ItemDTO itemDto = getItemDto();
        itemDto.setDescription(" ");

        mockMvc.perform(post(ITEMS_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).addItem(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createItem_whenAvailableIsNull_thenIsBadRequest() {
        final ItemDTO itemDto = getItemDto();
        itemDto.setAvailable(null);

        mockMvc.perform(post(ITEMS_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).addItem(anyLong(), any());
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

        verify(itemClient, never()).addComment(anyLong(), anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createItem_whenDescriptionIsNull_thenIsBadRequest() {
        final ItemDTO itemDto = getItemDto();
        itemDto.setDescription(null);

        mockMvc.perform(post(ITEMS_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).addItem(anyLong(), any());
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

        verify(itemClient, never()).addComment(anyLong(), anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createItem_whenNameIsNull_thenIsBadRequest() {
        final ItemDTO itemDto = getItemDto();
        itemDto.setName(null);

        mockMvc.perform(post(ITEMS_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).addItem(anyLong(), any());
    }
}