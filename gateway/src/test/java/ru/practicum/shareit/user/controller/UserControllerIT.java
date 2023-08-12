package ru.practicum.shareit.user.controller;

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
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDTO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.PatternsApp.USERS_PREFIX;

@WebMvcTest
class UserControllerIT extends AbstractTest {
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
    private final String urlPathWithIdVariable = "/users/{id}";

    @SneakyThrows
    @Test
    void createUser_whenEmailIsBad_thenStatusIsBadRequest() {
        final UserDTO userDTO = getUserDtoNoId();
        userDTO.setEmail("bademail.com");

        mockMvc.perform(post(USERS_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).addUser(any());
    }

    @SneakyThrows
    @Test
    void createUser_whenNameIsBlank_thenStatusIsBadRequest() {
        final UserDTO userDTO = getUserDtoNoId();
        userDTO.setName(" ");

        mockMvc.perform(post(USERS_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).addUser(any());
    }

    @SneakyThrows
    @Test
    void updateUser_whenEmailIsBad_thenStatusIsBadRequest() {
        final UserDTO userDTO = getUserDto();
        userDTO.setEmail("bademail.com");

        mockMvc.perform(patch(urlPathWithIdVariable, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).updateUser(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createUser_whenNameIsNull_thenStatusIsBadRequest() {
        final UserDTO userDTO = getUserDtoNoId();
        userDTO.setName(null);

        mockMvc.perform(post(USERS_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).addUser(any());
    }

    @SneakyThrows
    @Test
    void updateUser_whenEmailIsBlank_thenStatusIsBadRequest() {
        final UserDTO userDTO = getUserDto();
        userDTO.setEmail(" ");

        mockMvc.perform(patch(urlPathWithIdVariable, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).updateUser(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createUser_whenEmailIsNull_thenStatusIsBadRequest() {
        final UserDTO userDTO = getUserDtoNoId();
        userDTO.setEmail(null);

        mockMvc.perform(post(USERS_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).addUser(any());
    }

    @SneakyThrows
    @Test
    void createUser_whenEmailIsBlank_thenStatusIsBadRequest() {
        final UserDTO userDTO = getUserDtoNoId();
        userDTO.setEmail(" ");

        mockMvc.perform(post(USERS_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userClient, never()).addUser(any());
    }
}