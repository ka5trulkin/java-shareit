package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingOutDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.booking.*;
import ru.practicum.shareit.exception.item.ItemNotFoundException;
import ru.practicum.shareit.exception.user.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.model.Status.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest extends AbstractTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void addBooking() {
        final BookingOutDTO expected = getBookingOutDTO();
        final Booking booking = getBooking();
        final Long notOwnerId = booking.getOwnerId() + 1;

        when(userRepository.findById(any())).thenReturn(Optional.of(getUser()));
        when(itemRepository.findById(any())).thenReturn(Optional.of(getItem()));
        when(bookingRepository.save(any())).thenReturn(booking);

        final BookingOutDTO result = bookingService.addBooking(notOwnerId, getBookingDTO());

        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getStart()).isInstanceOf(LocalDateTime.class);
        assertThat(result.getEnd()).isInstanceOf(LocalDateTime.class);
        assertThat(result.getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.getBooker().getId()).isEqualTo(expected.getId());
        assertThat(result.getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.getItem().getName()).isEqualTo(expected.getItem().getName());

        final InOrder inOrder = inOrder(userRepository, itemRepository, bookingRepository);
        inOrder.verify(userRepository).findById(any());
        inOrder.verify(itemRepository).findById(any());
        inOrder.verify(bookingRepository).save(any());
    }

    @Test
    void addBooking_whenUserNotFound_thenThrowUserNotFoundException() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());


        assertThatThrownBy(() -> bookingService.addBooking(idOne, getBookingDTO()))
                .isInstanceOf(UserNotFoundException.class);

        verify(itemRepository, never()).findById(any());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBooking_whenItemNotFound_thenThrowItemNotFoundException() {
        when(userRepository.findById(any())).thenReturn(Optional.of(getUser()));
        when(itemRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.addBooking(idOne, getBookingDTO()))
                .isInstanceOf(ItemNotFoundException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBooking_whenItemIsNotAvailable_thenThrowItemIsNotAvailableException() {
        final Item item = getItem();
        item.setAvailable(false);

        when(userRepository.findById(any())).thenReturn(Optional.of(getUser()));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.addBooking(idOne, getBookingDTO()))
                .isInstanceOf(ItemIsNotAvailableException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBooking_whenBookerIsOwner_thenThrowBookerIsOwnerException() {
        final BookingDTO bookingDTO = getBookingDTO();
        final Long ownerId = bookingDTO.getBookerId();

        when(userRepository.findById(any())).thenReturn(Optional.of(getUser()));
        when(itemRepository.findById(any())).thenReturn(Optional.of(getItem()));

        assertThatThrownBy(() -> bookingService.addBooking(ownerId, bookingDTO))
                .isInstanceOf(BookerIsOwnerException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void approvalBooking_whenIsApproved_thenReturnStatusApproved() {
        final Booking booking = getBooking();
        booking.setStatus(WAITING);
        final Booking expectedBooking = getBooking();
        expectedBooking.setStatus(APPROVED);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(expectedBooking)).thenReturn(expectedBooking);

        final BookingOutDTO result = bookingService.approvalBooking(idOne, idOne, true);

        assertThat(result.getStatus()).isEqualTo(APPROVED);

        final InOrder inOrder = inOrder(bookingRepository);
        inOrder.verify(bookingRepository).findById(anyLong());
        inOrder.verify(bookingRepository).save(any());
    }

    @Test
    void approvalBooking_whenIsNotApproved_thenReturnStatusRejected() {
        final Booking booking = getBooking();
        booking.setStatus(WAITING);
        final Booking expectedBooking = getBooking();
        expectedBooking.setStatus(REJECTED);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(expectedBooking)).thenReturn(expectedBooking);

        final BookingOutDTO result = bookingService.approvalBooking(idOne, idOne, false);

        assertThat(result.getStatus()).isEqualTo(REJECTED);

        final InOrder inOrder = inOrder(bookingRepository);
        inOrder.verify(bookingRepository).findById(anyLong());
        inOrder.verify(bookingRepository).save(any());
    }

    @Test
    void approvalBooking_whenIsItemOwner_thenNotOwnerItemException() {
        final Booking booking = getBooking();
        final Long notItemOwnerId = booking.getOwnerId() + 1;
        booking.getItem().getOwner().setId(notItemOwnerId);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.approvalBooking(idOne, idOne, true))
                .isInstanceOf(NotOwnerItemException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void approvalBooking_whenStatusApproved_thenStatusCannotBeChangedException() {
        final Booking booking = getBooking();
        booking.setStatus(APPROVED);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.approvalBooking(idOne, idOne, true))
                .isInstanceOf(StatusCannotBeChangedException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void approvalBooking_whenBookingNotFound_thenBookingNotFoundException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.approvalBooking(idOne, idOne, true))
                .isInstanceOf(BookingNotFoundException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void approvalBooking_whenStatusRejected_thenStatusCannotBeChangedException() {
        final Booking booking = getBooking();
        booking.setStatus(REJECTED);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.approvalBooking(idOne, idOne, true))
                .isInstanceOf(StatusCannotBeChangedException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void getBookingByOwnerOrBooker() {
        final BookingOutDTO expected = getBookingOutDTO();

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(getBooking()));

        final BookingOutDTO result = bookingService.getBookingByOwnerOrBooker(idOne, idOne);

        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getStart()).isInstanceOf(LocalDateTime.class);
        assertThat(result.getEnd()).isInstanceOf(LocalDateTime.class);
        assertThat(result.getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.getBooker().getId()).isEqualTo(expected.getId());
        assertThat(result.getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void getBookingByOwnerOrBooker_whenBookingNotFound_thenBookingNotFoundException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.approvalBooking(idOne, idOne, true))
                .isInstanceOf(BookingNotFoundException.class);
    }

    @Test
    void getBookingByOwnerOrBooker_whenNotOwnerOrNotBooker_thenBookingNotFoundException() {
        final Booking booking = getBooking();
        final Long badOwner = booking.getOwnerId() + 1;
        final Long badBooker = booking.getBookerId() + 1;
        booking.getItem().getOwner().setId(badOwner);
        booking.getBooker().setId(badBooker);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.getBookingByOwnerOrBooker(idOne, idOne))
                .isInstanceOf(BookingNotFoundException.class);
    }

    @Test
    void getBookingsByBooker() {
        final BookingOutDTO expected = getBookingOutDTO();

        when(userRepository.existsById(any())).thenReturn(true);
        when(bookingRepository.findByBookerId(anyLong(), any()))
                .thenReturn(List.of(getBookingView()));

        final List<BookingOutDTO> result = bookingService.getBookingsByBooker(idOne, "ALL", 0, 1);

        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getStart()).isInstanceOf(LocalDateTime.class);
        assertThat(result.get(0).getEnd()).isInstanceOf(LocalDateTime.class);
        assertThat(result.get(0).getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.get(0).getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void getBookingsByBooker_whenUserNotFound_thenUserNotFoundException() {
        when(userRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> bookingService.getBookingsByBooker(idOne, "ALL", 0, 1))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getBookingsByOwner() {
        final BookingOutDTO expected = getBookingOutDTO();

        when(userRepository.existsById(any())).thenReturn(true);
        when(bookingRepository.findByOwnerId(any(), any())).thenReturn(List.of(getBookingView()));

        final List<BookingOutDTO> result = bookingService.getBookingsByOwner(idOne, "ALL", 0, 1);

        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getStart()).isInstanceOf(LocalDateTime.class);
        assertThat(result.get(0).getEnd()).isInstanceOf(LocalDateTime.class);
        assertThat(result.get(0).getStatus()).isEqualTo(expected.getStatus());
        assertThat(result.get(0).getBooker().getId()).isEqualTo(expected.getId());
        assertThat(result.get(0).getItem().getId()).isEqualTo(expected.getItem().getId());
        assertThat(result.get(0).getItem().getName()).isEqualTo(expected.getItem().getName());
    }

    @Test
    void getBookingsByOwner_whenUserNotFound_thenThrowUserNotFoundException() {
        when(userRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> bookingService.getBookingsByOwner(idOne, "ALL", 0, 1))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getBookingsByOwner_whenOutListIsEmpty_thenThrowOwnerHasNoItemsException() {
        when(userRepository.existsById(any())).thenReturn(true);
        when(bookingRepository.findByOwnerId(anyLong(), any())).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> bookingService.getBookingsByOwner(idOne, "ALL", 0, 1))
                .isInstanceOf(OwnerHasNoItemsException.class);
    }
}