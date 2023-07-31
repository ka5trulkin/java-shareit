package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.booking.model.Status.WAITING;

class BookingDTOMapperTest extends AbstractTest {
    @Test
    void toBooking() {
        final Booking expected = getBookingNoId();
        expected.setStatus(WAITING);
        final Booking actual = BookingDTOMapper.toBooking(getBookingDTO(), getUser(), getItem());

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getStart()).isEqualTo(expected.getStart());
        assertThat(actual.getEnd()).isEqualTo(expected.getEnd());
        assertThat(actual.getBookerId()).isEqualTo(expected.getBookerId());
        assertThat(actual.getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(actual.getBooker().getEmail()).isEqualTo(expected.getBooker().getEmail());
        assertThat(actual.getItemId()).isEqualTo(expected.getItemId());
        assertThat(actual.getItem().getIdOwner()).isEqualTo(expected.getItem().getIdOwner());
        assertThat(actual.getItem().getOwner().getName()).isEqualTo(expected.getItem().getOwner().getName());
        assertThat(actual.getItem().getOwner().getEmail()).isEqualTo(expected.getItem().getOwner().getEmail());
        assertThat(actual.getItem().getName()).isEqualTo(expected.getItem().getName());
        assertThat(actual.getItem().getDescription()).isEqualTo(expected.getItem().getDescription());
        assertThat(actual.getItem().isAvailable()).isEqualTo(expected.getItem().isAvailable());
        assertThat(actual.getItem().getRequestsId()).isEqualTo(expected.getItem().getRequestsId());
        assertThat(actual.getItem().getRequest().getDescription())
                .isEqualTo(expected.getItem().getRequest().getDescription());
        assertThat(actual.getItem().getRequest().getCreated())
                .isEqualTo(expected.getItem().getRequest().getCreated());
        assertThat(actual.getItem().getRequest().getOwner().getId())
                .isEqualTo(expected.getItem().getRequest().getOwner().getId());
        assertThat(actual.getItem().getRequest().getOwner().getName())
                .isEqualTo(expected.getItem().getRequest().getOwner().getName());
        assertThat(actual.getItem().getRequest().getOwner().getEmail())
                .isEqualTo(expected.getItem().getRequest().getOwner().getEmail());
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
    }

    @Test
    void fromViewToOut() {
        final BookingOutDTO expected = getBookingOutDTO();
        final BookingOutDTO actual = BookingDTOMapper.fromViewToOut(getBookingView());

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getStart()).isEqualTo(expected.getStart());
        assertThat(actual.getEnd()).isEqualTo(expected.getEnd());
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(actual.getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(actual.getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void fromViewToInfo() {
        final BookingInfoDTO expected = new BookingInfoDTO(idOne, idOne, dateTime, endTime);
        final BookingInfoDTO actual = BookingDTOMapper.fromViewToInfo(getBookingView());

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getBookerId()).isEqualTo(expected.getBookerId());
        assertThat(actual.getStart()).isEqualTo(expected.getStart());
        assertThat(actual.getEnd()).isEqualTo(expected.getEnd());
    }

    @Test
    void fromBooking() {
        final BookingOutDTO expected = getBookingOutDTO();
        final BookingOutDTO actual = BookingDTOMapper.fromBooking(getBooking());

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getStart()).isEqualTo(expected.getStart());
        assertThat(actual.getEnd()).isEqualTo(expected.getEnd());
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(actual.getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(actual.getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void fromCollection() {
        final BookingOutDTO expected = getBookingOutDTO();
        final List<BookingOutDTO> actual = BookingDTOMapper.fromCollection(List.of(getBookingView()));

        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual.get(0).getId()).isEqualTo(expected.getId());
        assertThat(actual.get(0).getStart()).isEqualTo(expected.getStart());
        assertThat(actual.get(0).getEnd()).isEqualTo(expected.getEnd());
        assertThat(actual.get(0).getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.get(0).getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(actual.get(0).getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(actual.get(0).getItem().getName()).isEqualTo(expected.getItem().getName());
    }
}