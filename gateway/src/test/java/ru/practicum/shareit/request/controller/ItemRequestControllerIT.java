package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestCreate;
import ru.practicum.shareit.user.client.UserClient;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.PatternsApp.ITEM_REQUEST_PREFIX;
import static ru.practicum.shareit.utils.PatternsApp.X_SHARER_USER_ID;

@WebMvcTest
class ItemRequestControllerIT extends AbstractTest {
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

    @SneakyThrows
    @Test
    void getAll_whenSizeLessThenOne_thenStatusIsServerError() {
        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, idOne)
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(0)))
                .andExpect(status().is5xxServerError());

        verify(itemRequestClient, never()).getAll(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void createUser_whenNameIsBlank_thenStatusIsBadRequest() {
        final ItemRequestCreate itemRequestCreate = new ItemRequestCreate(description);
        itemRequestCreate.setDescription(" ");

        mockMvc.perform(post(ITEM_REQUEST_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestCreate)))
                .andExpect(status().isBadRequest());

        verify(itemRequestClient, never()).addRequest(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void getAll_whenFromLessThenZero_thenStatusIsServerError() {
        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, idOne)
                        .param("from", String.valueOf(-1))
                        .param("size", String.valueOf(1)))
                .andExpect(status().is5xxServerError());

        verify(itemRequestClient, never()).getAll(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void createItemRequest_whenNameIsNull_thenStatusIsBadRequest() {
        final ItemRequestCreate itemRequestCreate = new ItemRequestCreate(description);
        itemRequestCreate.setDescription(null);

        mockMvc.perform(post(ITEM_REQUEST_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestCreate)))
                .andExpect(status().isBadRequest());

        verify(itemRequestClient, never()).addRequest(anyLong(), any());
    }
}