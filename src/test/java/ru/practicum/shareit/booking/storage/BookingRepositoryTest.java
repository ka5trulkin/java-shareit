package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookingRepositoryTest extends AbstractTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    BookingRepository bookingRepository;
    private Item item;
    private User user;
    private ItemRequest itemRequest;
    private Booking booking;
    private Long bookerId;
    private Long ownerId;
    private Long itemId;
    private Long bookingId;

    @BeforeEach
    void beforeEach() {
        user = getUserNoId();
        itemRequest = getItemRequestNoId();
        item = getItemNoId();
        userRepository.save(user);
        itemRequest.setOwner(user);
        itemRequestRepository.save(itemRequest);
        item.setOwner(user);
        item.setRequest(itemRequest);
        itemId = itemRepository.save(item).getId();
        booking = getBookingNoId();
        booking.setBooker(user);
        booking.setItem(item);
        bookingId = bookingRepository.save(booking).getId();
        bookerId = booking.getOwnerId();
        ownerId = item.getIdOwner();
    }

    @AfterEach
    void afterEach() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByBookerId() {
        final BookingView expected = getBookingView();
        expected.setId(bookingId);
        expected.setBooker(getUserView(user));
        expected.setItem(getItemView(item));
        final List<BookingView> result = bookingRepository.findByBookerId(bookerId, pageable);

        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getStart()).isEqualTo(expected.getStart());
        assertThat(result.get(0).getEnd()).isEqualTo(expected.getEnd());
        assertThat(result.get(0).getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(result.get(0).getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(result.get(0).getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.get(0).getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void findByBookerIdCurrentTime() {
        final BookingView expected = getBookingView();
        expected.setId(bookingId);
        expected.setBooker(getUserView(user));
        expected.setItem(getItemView(item));
        booking.setStart(nowTime.minusDays(1));
        booking.setEnd(nowTime.plusDays(1));
        bookingRepository.save(booking);
        final List<BookingView> result = bookingRepository
                .findByBookerIdCurrentTime(bookerId, nowTime, pageable);

        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getStart()).isBefore(nowTime);
        assertThat(result.get(0).getEnd()).isAfter(nowTime);
        assertThat(result.get(0).getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(result.get(0).getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(result.get(0).getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.get(0).getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void findByBookerIdPastTime() {
        final BookingView expected = getBookingView();
        expected.setId(bookingId);
        expected.setBooker(getUserView(user));
        expected.setItem(getItemView(item));
        booking.setStart(nowTime.minusDays(2));
        booking.setEnd(nowTime.minusDays(1));
        bookingRepository.save(booking);
        final List<BookingView> result = bookingRepository
                .findByBookerIdPastTime(bookerId, nowTime, pageable);

        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getStart()).isBefore(nowTime);
        assertThat(result.get(0).getEnd()).isBefore(nowTime);
        assertThat(result.get(0).getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(result.get(0).getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(result.get(0).getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.get(0).getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void findByBookerIdFutureTime() {
        final BookingView expected = getBookingView();
        expected.setId(bookingId);
        expected.setBooker(getUserView(user));
        expected.setItem(getItemView(item));
        booking.setStart(nowTime.plusDays(1));
        booking.setEnd(nowTime.plusDays(2));
        bookingRepository.save(booking);
        final List<BookingView> result = bookingRepository
                .findByBookerIdFutureTime(bookerId, nowTime, pageable);

        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getStart()).isAfter(nowTime);
        assertThat(result.get(0).getEnd()).isAfter(nowTime);
        assertThat(result.get(0).getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(result.get(0).getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(result.get(0).getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.get(0).getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void findByBookerIdAndStatus() {
        final BookingView expected = getBookingView();
        expected.setId(bookingId);
        expected.setBooker(getUserView(user));
        expected.setItem(getItemView(item));
        final List<BookingView> result = bookingRepository
                .findByBookerIdAndStatus(bookerId, booking.getStatus(), pageable);

        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getStart()).isEqualTo(expected.getStart());
        assertThat(result.get(0).getEnd()).isEqualTo(expected.getEnd());
        assertThat(result.get(0).getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(result.get(0).getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(result.get(0).getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.get(0).getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void findByOwnerId() {
        final BookingView expected = getBookingView();
        expected.setId(bookingId);
        expected.setBooker(getUserView(user));
        expected.setItem(getItemView(item));
        final List<BookingView> result = bookingRepository.findByOwnerId(ownerId, pageable);

        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getStart()).isEqualTo(expected.getStart());
        assertThat(result.get(0).getEnd()).isEqualTo(expected.getEnd());
        assertThat(result.get(0).getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(result.get(0).getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(result.get(0).getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.get(0).getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void findByOwnerIdCurrentTime() {
        final BookingView expected = getBookingView();
        expected.setId(bookingId);
        expected.setBooker(getUserView(user));
        expected.setItem(getItemView(item));
        booking.setStart(nowTime.minusDays(1));
        booking.setEnd(nowTime.plusDays(1));
        bookingRepository.save(booking);
        final List<BookingView> result = bookingRepository
                .findByOwnerIdCurrentTime(ownerId, nowTime, pageable);

        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getStart()).isBefore(nowTime);
        assertThat(result.get(0).getEnd()).isAfter(nowTime);
        assertThat(result.get(0).getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(result.get(0).getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(result.get(0).getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.get(0).getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void findByOwnerIdPastTime() {
        final BookingView expected = getBookingView();
        expected.setId(bookingId);
        expected.setBooker(getUserView(user));
        expected.setItem(getItemView(item));
        booking.setStart(nowTime.minusDays(2));
        booking.setEnd(nowTime.minusDays(1));
        bookingRepository.save(booking);
        final List<BookingView> result = bookingRepository
                .findByOwnerIdPastTime(ownerId, nowTime, pageable);

        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getStart()).isBefore(nowTime);
        assertThat(result.get(0).getEnd()).isBefore(nowTime);
        assertThat(result.get(0).getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(result.get(0).getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(result.get(0).getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.get(0).getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void findByOwnerIdFutureTime() {
        final BookingView expected = getBookingView();
        expected.setId(bookingId);
        expected.setBooker(getUserView(user));
        expected.setItem(getItemView(item));
        booking.setStart(nowTime.plusDays(1));
        booking.setEnd(nowTime.plusDays(2));
        bookingRepository.save(booking);
        final List<BookingView> result = bookingRepository
                .findByOwnerIdFutureTime(ownerId, nowTime, pageable);

        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getStart()).isAfter(nowTime);
        assertThat(result.get(0).getEnd()).isAfter(nowTime);
        assertThat(result.get(0).getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(result.get(0).getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(result.get(0).getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.get(0).getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void findByOwnerIdAndStatus() {
        final BookingView expected = getBookingView();
        expected.setId(bookingId);
        expected.setBooker(getUserView(user));
        expected.setItem(getItemView(item));
        final List<BookingView> result = bookingRepository
                .findByOwnerIdAndStatus(ownerId, booking.getStatus(), pageable);

        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getStart()).isEqualTo(expected.getStart());
        assertThat(result.get(0).getEnd()).isEqualTo(expected.getEnd());
        assertThat(result.get(0).getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(result.get(0).getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(result.get(0).getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.get(0).getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void findByItemIdIn() {
        final BookingView expected = getBookingView();
        expected.setId(bookingId);
        expected.setBooker(getUserView(user));
        expected.setItem(getItemView(item));
        final List<BookingView> result = bookingRepository.findByItemIdIn(List.of(itemId));

        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getStart()).isEqualTo(expected.getStart());
        assertThat(result.get(0).getEnd()).isEqualTo(expected.getEnd());
        assertThat(result.get(0).getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(result.get(0).getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(result.get(0).getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.get(0).getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void getLastBooking() {
        final BookingView expected = getBookingView();
        expected.setId(bookingId);
        expected.setBooker(getUserView(user));
        expected.setItem(getItemView(item));
        booking.setStart(nowTime.minusDays(2));
        booking.setEnd(nowTime.minusDays(1));
        bookingRepository.save(booking);
        final BookingView result = bookingRepository.getLastBooking(itemId, ownerId, nowTime)
                .orElseThrow();

        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getStart()).isBefore(nowTime);
        assertThat(result.getEnd()).isBefore(nowTime);
        assertThat(result.getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(result.getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(result.getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void getNextBooking() {
        final BookingView expected = getBookingView();
        expected.setId(bookingId);
        expected.setBooker(getUserView(user));
        expected.setItem(getItemView(item));
        booking.setStart(nowTime.plusDays(1));
        booking.setEnd(nowTime.plusDays(2));
        bookingRepository.save(booking);
        final BookingView result = bookingRepository.getNextBooking(itemId, ownerId, nowTime)
                .orElseThrow();

        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getStart()).isAfter(nowTime);
        assertThat(result.getEnd()).isAfter(nowTime);
        assertThat(result.getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(result.getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(result.getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void getByBookerIdAndItemId() {
        final BookingView expected = getBookingView();
        expected.setId(bookingId);
        expected.setBooker(getUserView(user));
        expected.setItem(getItemView(item));
        final BookingView result = bookingRepository
                .getByBookerIdAndItemId(bookerId, itemId, booking.getEnd().plusDays(1))
                .orElseThrow();

        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getStart()).isEqualTo(expected.getStart());
        assertThat(result.getEnd()).isEqualTo(expected.getEnd());
        assertThat(result.getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.getBooker().getId()).isEqualTo(expected.getBooker().getId());
        assertThat(result.getBooker().getName()).isEqualTo(expected.getBooker().getName());
        assertThat(result.getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.getItem().getName()).isEqualTo(expected.getItem().getName());
    }
}