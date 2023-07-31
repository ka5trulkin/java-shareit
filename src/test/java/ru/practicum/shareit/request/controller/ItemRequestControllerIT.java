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
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestOutWithItems;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.PatternsApp.X_SHARER_USER_ID;

@WebMvcTest
class ItemRequestControllerIT extends AbstractTest {
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
    private final String urlPath = "/requests";

    @SneakyThrows
    @Test
    void createItemRequest() {
        final ItemRequestCreate itemRequestCreate = new ItemRequestCreate(description);
        final ItemRequestDTO expectedDTO = getItemRequestDto();

        when(requestService.addRequest(idOne, itemRequestCreate)).thenReturn(expectedDTO);

        String result = mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestCreate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(expectedDTO));
    }

    @SneakyThrows
    @Test
    void createItemRequest_whenNameIsNull_thenStatusIsBadRequest() {
        final ItemRequestCreate itemRequestCreate = new ItemRequestCreate(description);
        itemRequestCreate.setDescription(null);

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestCreate)))
                .andExpect(status().isBadRequest());

        verify(requestService, never()).addRequest(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createUser_whenNameIsBlank_thenStatusIsBadRequest() {
        final ItemRequestCreate itemRequestCreate = new ItemRequestCreate(description);
        itemRequestCreate.setDescription(" ");

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestCreate)))
                .andExpect(status().isBadRequest());

        verify(requestService, never()).addRequest(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void getAllToOwner() {
        final ItemRequestOutWithItems expectedOut = getItemRequestOutWithItems();

        when(requestService.getAllToOwner(anyLong())).thenReturn(List.of(expectedOut));

        String result = mockMvc.perform(get(urlPath)
                        .header(X_SHARER_USER_ID, idOne))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(List.of(expectedOut)));
    }

    @SneakyThrows
    @Test
    void getAll() {
        final ItemRequestOutWithItems expectedOut = getItemRequestOutWithItems();

        when(requestService.getAll(anyLong(), anyInt(), anyInt())).thenReturn(List.of(expectedOut));

        String result = mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, idOne)
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(1)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(List.of(expectedOut)));
    }

    @SneakyThrows
    @Test
    void getAll_whenFromLessThenZero_thenStatusIsServerError() {
        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, idOne)
                        .param("from", String.valueOf(-1))
                        .param("size", String.valueOf(1)))
                .andExpect(status().is5xxServerError());

        verify(requestService, never()).getAll(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getAll_whenSizeLessThenOne_thenStatusIsServerError() {
        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, idOne)
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(0)))
                .andExpect(status().is5xxServerError());

        verify(requestService, never()).getAll(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getAll_whenSizeMoreThenFifty_thenStatusIsServerError() {
        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, idOne)
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(51)))
                .andExpect(status().is5xxServerError());

        verify(requestService, never()).getAll(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getRequest() {
        final ItemRequestOutWithItems expectedOut = getItemRequestOutWithItems();

        when(requestService.getRequest(anyLong(), anyLong())).thenReturn(expectedOut);

        String result = mockMvc.perform(get("/requests/{requestId}", idOne)
                        .header(X_SHARER_USER_ID, idOne))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(expectedOut));
    }
}