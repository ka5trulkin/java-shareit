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
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerIT extends AbstractTest {
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
    private ItemRequestService itemRequestService;
    private final String urlPath = "/users";
    private final String urlPathWithIdVariable = "/users/{id}";

    @SneakyThrows
    @Test
    void createUser() {
        final UserDTO userDTO = getUserDtoNoId();
        final UserDTO expectedDTO = getUserDto();

        when(userService.addUser(userDTO)).thenReturn(expectedDTO);

        String result = mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(expectedDTO));
    }

    @SneakyThrows
    @Test
    void createUser_whenNameIsNull_thenStatusIsBadRequest() {
        final UserDTO userDTO = getUserDtoNoId();
        userDTO.setName(null);

        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addUser(any());
    }

    @SneakyThrows
    @Test
    void createUser_whenNameIsBlank_thenStatusIsBadRequest() {
        final UserDTO userDTO = getUserDtoNoId();
        userDTO.setName(" ");

        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addUser(any());
    }

    @SneakyThrows
    @Test
    void createUser_whenEmailIsBad_thenStatusIsBadRequest() {
        final UserDTO userDTO = getUserDtoNoId();
        userDTO.setEmail("bademail.com");

        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addUser(any());
    }

    @SneakyThrows
    @Test
    void createUser_whenEmailIsNull_thenStatusIsBadRequest() {
        final UserDTO userDTO = getUserDtoNoId();
        userDTO.setEmail(null);

        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addUser(any());
    }

    @SneakyThrows
    @Test
    void createUser_whenEmailIsBlank_thenStatusIsBadRequest() {
        final UserDTO userDTO = getUserDtoNoId();
        userDTO.setEmail(" ");

        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addUser(any());
    }

    @SneakyThrows
    @Test
    void updateUser() {
        final UserDTO userDTO = getUserDto();

        when(userService.updateUser(idOne, userDTO)).thenReturn(userDTO);

        String result = mockMvc.perform(patch(urlPathWithIdVariable, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(userDTO));
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

        verify(userService, never()).updateUser(anyLong(), any());
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

        verify(userService, never()).updateUser(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void getUserTest() {
        final UserDTO userDTO = getUserDto();

        when(userService.getUser(idOne)).thenReturn(userDTO);

        String result = mockMvc.perform(get(urlPathWithIdVariable, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(userDTO));
    }

    @SneakyThrows
    @Test
    void deleteUser() {
        doNothing().when(userService).deleteUser(idOne);

        mockMvc.perform(delete(urlPathWithIdVariable, idOne))
                .andExpect(status().isOk());

        verify(userService).deleteUser(anyLong());
    }

    @SneakyThrows
    @Test
    void getAllUsers() {
        final UserDTO userDTO = getUserDto();

        when(userService.getAllUsers()).thenReturn(List.of(userDTO));

        String result = mockMvc.perform(get(urlPath, idOne))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(List.of(userDTO)));
    }
}