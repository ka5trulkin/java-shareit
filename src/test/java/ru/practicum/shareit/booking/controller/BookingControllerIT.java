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
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingOutDTO;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.PatternsApp.X_SHARER_USER_ID;

@WebMvcTest
class BookingControllerIT extends AbstractTest {
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
    private final String urlPath = "/bookings";
    private final String urlPathId = String.join("/", urlPath, "{bookingId}");
    private final String urlPathOwner = String.join("/", urlPath, "owner");

    private BookingOutDTO getBookingOutDtoOnCreate() {
        final BookingOutDTO booking = getBookingOutDTO();
        booking.setStart(now().plusMinutes(1));
        booking.setEnd(now().plusDays(1));
        return booking;
    }

    private BookingDTO getBookingDtoOnCreate() {
        final BookingDTO booking = getBookingDTO();
        booking.setStart(now().plusMinutes(1));
        booking.setEnd(now().plusDays(1));
        return booking;
    }

    @SneakyThrows
    @Test
    void createBooking() {
        final BookingOutDTO expected = getBookingOutDtoOnCreate();
        final BookingDTO bookingDTO = getBookingDtoOnCreate();

        when(bookingService.addBooking(anyLong(), any())).thenReturn(expected);

        String actual = mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(objectMapper.writeValueAsString(expected));
    }

    @SneakyThrows
    @Test
    void createBooking_whenItemIdIsNotPositive_thenBadRequest() {
        final BookingDTO bookingDTO = getBookingDtoOnCreate();
        bookingDTO.setItemId(0L);

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createBooking_whenStartIsNull_thenBadRequest() {
        final BookingDTO bookingDTO = getBookingDtoOnCreate();
        bookingDTO.setStart(null);

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createBooking_whenStartIsPastTime_thenBadRequest() {
        final BookingDTO bookingDTO = getBookingDtoOnCreate();
        bookingDTO.setStart(now().minusMinutes(1));

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createBooking_whenEndIsNull_thenBadRequest() {
        final BookingDTO bookingDTO = getBookingDtoOnCreate();
        bookingDTO.setEnd(null);

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createBooking_whenEndIsPastTime_thenBadRequest() {
        final BookingDTO bookingDTO = getBookingDtoOnCreate();
        bookingDTO.setEnd(now().minusMinutes(1));

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createBooking_whenStartIsLaterThanEnd_thenBadRequest() {
        final BookingDTO bookingDTO = getBookingDtoOnCreate();
        bookingDTO.setEnd(now().plusMinutes(1));
        bookingDTO.setStart(now().plusMinutes(2));

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void createBooking_whenStartIsEqualEnd_thenBadRequest() {
        final BookingDTO bookingDTO = getBookingDtoOnCreate();
        bookingDTO.setStart(now().plusMinutes(1));
        bookingDTO.setEnd(bookingDTO.getStart());

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).addBooking(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void approvalBooking() {
        final BookingOutDTO expected = getBookingOutDTO();
        when(bookingService.approvalBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(expected);

        String actual = mockMvc.perform(patch(urlPathId, idOne)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(objectMapper.writeValueAsString(expected));
    }

    @SneakyThrows
    @Test
    void getBookingByOwnerOrBooker() {
        final BookingOutDTO expected = getBookingOutDTO();

        when(bookingService.getBookingByOwnerOrBooker(anyLong(), anyLong())).thenReturn(expected);

        String actual = mockMvc.perform(get(urlPathId, idOne)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("bookingId", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(objectMapper.writeValueAsString(expected));
    }

    @SneakyThrows
    @Test
    void getBookingsByBooker() {
        final List<BookingOutDTO> expected = List.of(getBookingOutDTO());

        when(bookingService.getBookingsByBooker(anyLong(), any(), anyInt(), anyInt())).thenReturn(expected);

        String actual = mockMvc.perform(get(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("state", "all")
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(objectMapper.writeValueAsString(expected));
    }

    @SneakyThrows
    @Test
    void getBookingsByBooker_whenFromIsLowerThanZero_thenIsServerError() {
        mockMvc.perform(get(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("from", "-1")
                        .param("size", "1"))
                .andExpect(status().is5xxServerError());

        verify(bookingService, never()).getBookingsByBooker(anyLong(), any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getBookingsByBooker_whenSizeIsLowerThanOne_thenIsServerError() {
        mockMvc.perform(get(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().is5xxServerError());

        verify(bookingService, never()).getBookingsByBooker(anyLong(), any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getBookingsByBooker_whenSizeIsMoreThanFifty_thenIsServerError() {
        mockMvc.perform(get(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("from", "0")
                        .param("size", "51"))
                .andExpect(status().is5xxServerError());

        verify(bookingService, never()).getBookingsByBooker(anyLong(), any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getBookingsByOwner() {
        final List<BookingOutDTO> expected = List.of(getBookingOutDTO());

        when(bookingService.getBookingsByOwner(anyLong(), any(), anyInt(), anyInt())).thenReturn(expected);

        String actual = mockMvc.perform(get(urlPathOwner)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("state", "all")
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(objectMapper.writeValueAsString(expected));
    }

    @SneakyThrows
    @Test
    void getBookingsByOwner_whenFromIsLowerThanZero_thenIsServerError() {
        mockMvc.perform(get(urlPathOwner)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("from", "-1")
                        .param("size", "1"))
                .andExpect(status().is5xxServerError());

        verify(bookingService, never()).getBookingsByOwner(anyLong(), any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getBookingsByOwner_whenSizeIsLowerThanOne_thenIsServerError() {
        mockMvc.perform(get(urlPathOwner)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().is5xxServerError());

        verify(bookingService, never()).getBookingsByOwner(anyLong(), any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getBookingsByOwner_whenSizeIsMoreThanFifty_thenIsServerError() {
        mockMvc.perform(get(urlPathOwner)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("from", "0")
                        .param("size", "51"))
                .andExpect(status().is5xxServerError());

        verify(bookingService, never()).getBookingsByOwner(anyLong(), any(), anyInt(), anyInt());
    }
}