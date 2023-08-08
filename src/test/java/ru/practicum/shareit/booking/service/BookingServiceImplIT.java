package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingOutDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.booking.model.Status.WAITING;

@SpringBootTest
class BookingServiceImplIT extends AbstractTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingService bookingService;
    private Item item;
    private User user;
    private ItemRequest itemRequest;
    private Booking booking;
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
    }

    @AfterEach
    void afterEach() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void addBooking() {
        final User notBookingOwner = getUserNoId();
        final Long expectedBookingId = bookingId + 1;
        notBookingOwner.setEmail("new@email.com");
        final Long newBookerId = userRepository.save(notBookingOwner).getId();
        final BookingDTO bookingDTO = getBookingDTO();
        bookingDTO.setItemId(itemId);

        final BookingOutDTO actual = bookingService.addBooking(newBookerId, bookingDTO);

        assertThat(actual.getId()).isEqualTo(expectedBookingId);
        assertThat(actual.getStart()).isEqualTo(dateTime);
        assertThat(actual.getEnd()).isEqualTo(endTime);
        assertThat(actual.getStatus()).isEqualTo(WAITING);
        assertThat(actual.getBooker().getId()).isEqualTo(newBookerId);
        assertThat(actual.getItem().getId()).isEqualTo(itemId);
        assertThat(actual.getItem().getName()).isEqualTo(item.getName());
    }
}