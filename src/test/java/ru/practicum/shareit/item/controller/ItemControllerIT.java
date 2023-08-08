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

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.PatternsApp.*;

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
        final ItemDTO expected = getItemDto();

        when(itemService.addItem(anyLong(), any())).thenReturn(expected);

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expected)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.ownerId").value(expected.getOwnerId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.description").value(expected.getDescription()))
                .andExpect(jsonPath("$.available").value(expected.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(expected.getRequestId()));
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
        final ItemDTO expected = getItemDto();

        when(itemService.updateItem(anyLong(), anyLong(), any())).thenReturn(expected);

        mockMvc.perform(patch(urlPathId, idOne)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expected)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.ownerId").value(expected.getOwnerId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.description").value(expected.getDescription()))
                .andExpect(jsonPath("$.available").value(expected.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(expected.getRequestId()));
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
        final ItemOut expected = getItemOut();

        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(expected);

        mockMvc.perform(get(urlPathId, idOne)
                        .header(X_SHARER_USER_ID, idOne))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.ownerId").value(expected.getOwnerId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.description").value(expected.getDescription()))
                .andExpect(jsonPath("$.available").value(expected.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(expected.getRequestId()));
    }

    @SneakyThrows
    @Test
    void getItemsByOwner() {
        final List<ItemOut> list = List.of(getItemOut());
        final ItemOut expected = list.get(0);

        when(itemService.getItemsByOwner(anyLong(), anyInt(), anyInt())).thenReturn(list);

        mockMvc.perform(get(urlPath)
                        .header(X_SHARER_USER_ID, idOne))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(expected.getId()))
                .andExpect(jsonPath("$[0].ownerId").value(expected.getOwnerId()))
                .andExpect(jsonPath("$[0].name").value(expected.getName()))
                .andExpect(jsonPath("$[0].description").value(expected.getDescription()))
                .andExpect(jsonPath("$[0].available").value(expected.getAvailable()))
                .andExpect(jsonPath("$[0].requestId").value(expected.getRequestId()));
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
    void getItemBySearch() {
        final List<ItemOut> list = List.of(getItemOut());
        final ItemOut expected = list.get(0);

        when(itemService.getItemsBySearch(any(), anyInt(), anyInt())).thenReturn(list);

        mockMvc.perform(get(urlPathSearch)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("text", text))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(expected.getId()))
                .andExpect(jsonPath("$[0].ownerId").value(expected.getOwnerId()))
                .andExpect(jsonPath("$[0].name").value(expected.getName()))
                .andExpect(jsonPath("$[0].description").value(expected.getDescription()))
                .andExpect(jsonPath("$[0].available").value(expected.getAvailable()))
                .andExpect(jsonPath("$[0].requestId").value(expected.getRequestId()));
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
    void createComment() {
        final CommentDTO expected = getCommentDto();

        when(itemService.addComment(anyLong(), anyLong(), any())).thenReturn(expected);

        mockMvc.perform(post(urlPathIdComment, idOne)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expected)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.text").value(expected.getText()))
                .andExpect(jsonPath("$.authorName").value(expected.getAuthorName()))
                .andExpect(jsonPath("$.created").value(expected.getCreated().format(ofPattern(DATE_TIME_MS))));
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