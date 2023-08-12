package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.user.client.UserClient;

import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.PatternsApp.BOOKINGS_PREFIX;
import static ru.practicum.shareit.utils.PatternsApp.X_SHARER_USER_ID;

@WebMvcTest
class BookingControllerIT extends AbstractTest {
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
    void createBooking_whenEndIsPastTime_thenBadRequest() {
        final BookingDTO bookingDTO = getBookingDTO();
        bookingDTO.setEnd(now().minusDays(1));

        mockMvc.perform(post(BOOKINGS_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).addBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createBooking_whenStartIsPastTime_thenBadRequest() {
        final BookingDTO bookingDTO = getBookingDTO();
        bookingDTO.setStart(now().minusDays(1));

        mockMvc.perform(post(BOOKINGS_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).addBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createBooking_whenItemIdIsNotPositive_thenBadRequest() {
        final BookingDTO bookingDTO = getBookingDTO();
        bookingDTO.setItemId(0L);

        mockMvc.perform(post(BOOKINGS_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).addBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createBooking_whenStartIsNull_thenBadRequest() {
        final BookingDTO bookingDTO = getBookingDTO();
        bookingDTO.setStart(null);

        mockMvc.perform(post(BOOKINGS_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).addBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createBooking_whenStartIsEqualEnd_thenBadRequest() {
        final BookingDTO bookingDTO = getBookingDTO();
        bookingDTO.setStart(now().plusDays(1));
        bookingDTO.setEnd(bookingDTO.getStart());

        mockMvc.perform(post(BOOKINGS_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).addBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createBooking_whenStartIsLaterThanEnd_thenBadRequest() {
        final BookingDTO bookingDTO = getBookingDTO();
        bookingDTO.setEnd(now().plusMinutes(1));
        bookingDTO.setStart(now().plusMinutes(2));

        mockMvc.perform(post(BOOKINGS_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).addBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createBooking_whenEndIsNull_thenBadRequest() {
        final BookingDTO bookingDTO = getBookingDTO();
        bookingDTO.setEnd(null);

        mockMvc.perform(post(BOOKINGS_PREFIX)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingClient, never()).addBooking(anyLong(), any());
    }
}