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
import ru.practicum.shareit.item.dto.item.ItemInRequest;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestOutWithItems;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.PatternsApp.*;

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

    @SneakyThrows
    @Test
    void createItemRequest() {
        final ItemRequestCreate itemRequestCreate = new ItemRequestCreate(description);
        final ItemRequestDTO expected = getItemRequestDto();

        when(requestService.addRequest(idOne, itemRequestCreate)).thenReturn(expected);

        mockMvc.perform(post(ITEM_REQUEST_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.description").value(expected.getDescription()))
                .andExpect(jsonPath("$.created").value(expected.getCreated().format(ofPattern(DATE_TIME))));
    }

    @SneakyThrows
    @Test
    void getAllToOwner() {
        final ItemRequestOutWithItems expected = getItemRequestOutWithItems();
        final ItemInRequest expectedItem = expected.getItems().get(0);

        when(requestService.getAllToOwner(anyLong())).thenReturn(List.of(expected));

        mockMvc.perform(get(ITEM_REQUEST_PREFIX)
                        .header(X_SHARER_USER_ID, idOne))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(expected.getId()))
                .andExpect(jsonPath("$[0].description").value(expected.getDescription()))
                .andExpect(jsonPath("$[0].created").value(expected.getCreated().format(ofPattern(DATE_TIME))))
                .andExpect(jsonPath("$[0].items").isArray())
                .andExpect(jsonPath("$[0].items").isNotEmpty())
                .andExpect(jsonPath("$[0].items[0].id").value(expectedItem.getId()))
                .andExpect(jsonPath("$[0].items[0].name").value(expectedItem.getName()))
                .andExpect(jsonPath("$[0].items[0].description").value(expectedItem.getDescription()))
                .andExpect(jsonPath("$[0].items[0].available").value(expectedItem.getAvailable()))
                .andExpect(jsonPath("$[0].items[0].requestId").value(expectedItem.getRequestId()));
    }

    @SneakyThrows
    @Test
    void getAll() {
        final ItemRequestOutWithItems expected = getItemRequestOutWithItems();
        final ItemInRequest expectedItem = expected.getItems().get(0);

        when(requestService.getAll(anyLong(), anyInt(), anyInt())).thenReturn(List.of(expected));

        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, idOne)
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(expected.getId()))
                .andExpect(jsonPath("$[0].description").value(expected.getDescription()))
                .andExpect(jsonPath("$[0].created").value(expected.getCreated().format(ofPattern(DATE_TIME))))
                .andExpect(jsonPath("$[0].items").isArray())
                .andExpect(jsonPath("$[0].items").isNotEmpty())
                .andExpect(jsonPath("$[0].items[0].id").value(expectedItem.getId()))
                .andExpect(jsonPath("$[0].items[0].name").value(expectedItem.getName()))
                .andExpect(jsonPath("$[0].items[0].description").value(expectedItem.getDescription()))
                .andExpect(jsonPath("$[0].items[0].available").value(expectedItem.getAvailable()))
                .andExpect(jsonPath("$[0].items[0].requestId").value(expectedItem.getRequestId()));
    }

    @SneakyThrows
    @Test
    void getRequest() {
        final ItemRequestOutWithItems expected = getItemRequestOutWithItems();
        final ItemInRequest expectedItem = expected.getItems().get(0);

        when(requestService.getRequest(anyLong(), anyLong())).thenReturn(expected);

        mockMvc.perform(get("/requests/{requestId}", idOne)
                        .header(X_SHARER_USER_ID, idOne))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.description").value(expected.getDescription()))
                .andExpect(jsonPath("$.created").value(expected.getCreated().format(ofPattern(DATE_TIME))))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items[0].id").value(expectedItem.getId()))
                .andExpect(jsonPath("$.items[0].name").value(expectedItem.getName()))
                .andExpect(jsonPath("$.items[0].description").value(expectedItem.getDescription()))
                .andExpect(jsonPath("$.items[0].available").value(expectedItem.getAvailable()))
                .andExpect(jsonPath("$.items[0].requestId").value(expectedItem.getRequestId()));
    }
}