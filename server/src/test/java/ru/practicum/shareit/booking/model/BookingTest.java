package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractTest;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.booking.model.Status.APPROVED;
import static ru.practicum.shareit.booking.model.Status.REJECTED;

class BookingTest extends AbstractTest {

    @Test
    void getBookerId() {
        final Booking booking = getBooking();

        assertThat(booking.getBookerId()).isEqualTo(idOne);
    }

    @Test
    void getItemId() {
        final Booking booking = getBooking();

        assertThat(booking.getItemId()).isEqualTo(idOne);
    }

    @Test
    void getItemName() {
        final Booking booking = getBooking();

        assertThat(booking.getItemName()).isEqualTo(name);
    }

    @Test
    void getOwnerId() {
        final Booking booking = getBooking();

        assertThat(booking.getOwnerId()).isEqualTo(idOne);
    }

    @Test
    void setApprovedStatus() {
        final Booking booking = getBooking();
        booking.setApprovedStatus(true);

        assertThat(booking.getStatus()).isEqualTo(APPROVED);

        booking.setApprovedStatus(false);

        assertThat(booking.getStatus()).isEqualTo(REJECTED);
    }

    @Test
    void testEquals() {
        final Booking booking1 = getBooking();
        final Booking booking2 = getBooking();

        assertThat(booking1).isEqualTo(booking2);
        assertThat(booking1).isEqualTo(booking1);
        assertThat(booking1).isNotEqualTo(null);
        assertThat(booking1).isNotEqualTo(getUser());
    }

    @Test
    void testHashCode() {
        final Booking booking = getBooking();

        assertThat(booking.hashCode()).isEqualTo(booking.hashCode());
    }
}