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
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.PatternsApp.DATE_TIME;
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

        mockMvc.perform(post(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.start").value(expected.getStart().format(ofPattern(DATE_TIME))))
                .andExpect(jsonPath("$.end").value(expected.getEnd().format(ofPattern(DATE_TIME))))
                .andExpect(jsonPath("$.status").value(expected.getStatus().name()))
                .andExpect(jsonPath("$.booker.id").value(expected.getBooker().getId()))
                .andExpect(jsonPath("$.item.id").value(expected.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(expected.getItem().getName()));
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

        mockMvc.perform(patch(urlPathId, idOne)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.start").value(expected.getStart().format(ofPattern(DATE_TIME))))
                .andExpect(jsonPath("$.end").value(expected.getEnd().format(ofPattern(DATE_TIME))))
                .andExpect(jsonPath("$.status").value(expected.getStatus().name()))
                .andExpect(jsonPath("$.booker.id").value(expected.getBooker().getId()))
                .andExpect(jsonPath("$.item.id").value(expected.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(expected.getItem().getName()));

    }

    @SneakyThrows
    @Test
    void getBookingByOwnerOrBooker() {
        final BookingOutDTO expected = getBookingOutDTO();

        when(bookingService.getBookingByOwnerOrBooker(anyLong(), anyLong())).thenReturn(expected);

        mockMvc.perform(get(urlPathId, idOne)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("bookingId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.start").value(expected.getStart().format(ofPattern(DATE_TIME))))
                .andExpect(jsonPath("$.end").value(expected.getEnd().format(ofPattern(DATE_TIME))))
                .andExpect(jsonPath("$.status").value(expected.getStatus().name()))
                .andExpect(jsonPath("$.booker.id").value(expected.getBooker().getId()))
                .andExpect(jsonPath("$.item.id").value(expected.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(expected.getItem().getName()));

    }

    @SneakyThrows
    @Test
    void getBookingsByBooker() {
        final List<BookingOutDTO> list = List.of(getBookingOutDTO());
        final BookingOutDTO expected = list.get(0);

        when(bookingService.getBookingsByBooker(anyLong(), any(), anyInt(), anyInt())).thenReturn(list);

        mockMvc.perform(get(urlPath)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("state", "all")
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(expected.getId()))
                .andExpect(jsonPath("$[0].start").value(expected.getStart().format(ofPattern(DATE_TIME))))
                .andExpect(jsonPath("$[0].end").value(expected.getEnd().format(ofPattern(DATE_TIME))))
                .andExpect(jsonPath("$[0].status").value(expected.getStatus().name()))
                .andExpect(jsonPath("$[0].booker.id").value(expected.getBooker().getId()))
                .andExpect(jsonPath("$[0].item.id").value(expected.getItem().getId()))
                .andExpect(jsonPath("$[0].item.name").value(expected.getItem().getName()));
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
    void getBookingsByOwner() {
        final List<BookingOutDTO> list = List.of(getBookingOutDTO());
        final BookingOutDTO expected = list.get(0);

        when(bookingService.getBookingsByOwner(anyLong(), any(), anyInt(), anyInt())).thenReturn(list);

        mockMvc.perform(get(urlPathOwner)
                        .header(X_SHARER_USER_ID, idOne)
                        .param("state", "all")
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(expected.getId()))
                .andExpect(jsonPath("$[0].start").value(expected.getStart().format(ofPattern(DATE_TIME))))
                .andExpect(jsonPath("$[0].end").value(expected.getEnd().format(ofPattern(DATE_TIME))))
                .andExpect(jsonPath("$[0].status").value(expected.getStatus().name()))
                .andExpect(jsonPath("$[0].booker.id").value(expected.getBooker().getId()))
                .andExpect(jsonPath("$[0].item.id").value(expected.getItem().getId()))
                .andExpect(jsonPath("$[0].item.name").value(expected.getItem().getName()));
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
}