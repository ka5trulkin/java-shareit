package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.booking.dto.BookingOutDTO;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest extends AbstractTest {
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingController controller;

    @Test
    void createBooking() {
        final BookingOutDTO expected = getBookingOutDTO();

        when(bookingService.addBooking(anyLong(), any())).thenReturn(expected);

        assertThat(controller.createBooking(idOne, getBookingDTO())).isEqualTo(expected);
    }

    @Test
    void approvalBooking() {
        final BookingOutDTO expected = getBookingOutDTO();

        when(bookingService.approvalBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(expected);

        assertThat(controller.approvalBooking(idOne, idOne, true)).isEqualTo(expected);
    }

    @Test
    void getBookingByOwnerOrBooker() {
        final BookingOutDTO expected = getBookingOutDTO();

        when(bookingService.getBookingByOwnerOrBooker(anyLong(), anyLong())).thenReturn(expected);

        assertThat(controller.getBookingByOwnerOrBooker(idOne, idOne)).isEqualTo(expected);
    }

    @Test
    void getBookingsByBooker() {
        final List<BookingOutDTO> expected = List.of(getBookingOutDTO());

        when(bookingService.getBookingsByBooker(anyLong(), any(), anyInt(), anyInt())).thenReturn(expected);

        final List<BookingOutDTO> actual = controller.getBookingsByBooker(idOne, "all", 0, 1);

        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getBookingsByOwner() {
        final List<BookingOutDTO> expected = List.of(getBookingOutDTO());

        when(bookingService.getBookingsByOwner(anyLong(), any(), anyInt(), anyInt())).thenReturn(expected);

        final List<BookingOutDTO> actual = controller.getBookingsByOwner(idOne, "all", 0, 1);

        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual).isEqualTo(expected);
    }
}