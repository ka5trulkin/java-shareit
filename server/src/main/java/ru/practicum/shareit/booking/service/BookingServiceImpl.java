package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingDTOMapper;
import ru.practicum.shareit.booking.dto.BookingOutDTO;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StateParam;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.booking.*;
import ru.practicum.shareit.exception.item.ItemNotFoundException;
import ru.practicum.shareit.exception.user.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.utils.PageApp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.booking.BookingLogMessage.*;
import static ru.practicum.shareit.booking.model.Status.REJECTED;
import static ru.practicum.shareit.booking.model.Status.WAITING;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private void checkIsAvailableItem(Booking booking) {
        if (!booking.getItem().isAvailable()) {
            throw new ItemIsNotAvailableException(booking.getItemName());
        }
    }

    private void checkIsOwnerOfItem(Long ownerId, Booking booking) {
        if (!Objects.equals(booking.getOwnerId(), ownerId)) {
            throw new NotOwnerItemException(ownerId, booking.getItemId());
        }
    }

    private void checkBookerIsOwner(Long bookerId, Booking booking) {
        if (Objects.equals(bookerId, booking.getOwnerId())) {
            throw new BookerIsOwnerException(bookerId);
        }
    }

    private void checkWhetherOwnerOrBooker(Long userId, Booking booking) {
        if (!Objects.equals(booking.getOwnerId(), userId)
                && !Objects.equals(booking.getBookerId(), userId)) {
            throw new BookingNotFoundException(booking.getId());
        }
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    private void checkBookingStatus(Booking booking) {
        if (booking.getStatus() != WAITING) {
            throw new StatusCannotBeChangedException(booking.getStatus());
        }
    }

    private List<BookingOutDTO> getOutDTOListByStateParam(Long userId, String stateParam, int from, int size) {
        final StateParam state = StateParam.valueOf(stateParam);
        final List<BookingView> bookingViews;
        final LocalDateTime time = LocalDateTime.now();
        final Pageable pageable = PageApp.ofStartingIndex(from, size);
        switch (state) {
            case ALL:
                bookingViews = bookingRepository.findByBookerId(userId, pageable);
                break;
            case CURRENT:
                bookingViews = bookingRepository.findByBookerIdCurrentTime(userId, time, pageable);
                break;
            case PAST:
                bookingViews = bookingRepository.findByBookerIdPastTime(userId, time, pageable);
                break;
            case FUTURE:
                bookingViews = bookingRepository.findByBookerIdFutureTime(userId, time, pageable);
                break;
            case WAITING:
                bookingViews = bookingRepository.findByBookerIdAndStatus(userId, WAITING, pageable);
                break;
            case REJECTED:
                bookingViews = bookingRepository.findByBookerIdAndStatus(userId, REJECTED, pageable);
                break;
            default:
                throw new InternalError();
        }
        return BookingDTOMapper.fromCollection(bookingViews);
    }

    private List<BookingOutDTO> getOutDTOListByStateParamToOwner(Long ownerId, String stateParam,
                                                                 Integer from, Integer size) {
        final StateParam state = StateParam.valueOf(stateParam);
        final List<BookingView> bookingViews;
        final LocalDateTime time = LocalDateTime.now();
        final Pageable pageable = PageApp.ofStartingIndex(from, size);
        switch (state) {
            case ALL:
                bookingViews = bookingRepository.findByOwnerId(ownerId, pageable);
                break;
            case CURRENT:
                bookingViews = bookingRepository.findByOwnerIdCurrentTime(ownerId, time, pageable);
                break;
            case PAST:
                bookingViews = bookingRepository.findByOwnerIdPastTime(ownerId, time, pageable);
                break;
            case FUTURE:
                bookingViews = bookingRepository.findByOwnerIdFutureTime(ownerId, time, pageable);
                break;
            case WAITING:
                bookingViews = bookingRepository.findByOwnerIdAndStatus(ownerId, WAITING, pageable);
                break;
            case REJECTED:
                bookingViews = bookingRepository.findByOwnerIdAndStatus(ownerId, REJECTED, pageable);
                break;
            default:
                throw new InternalError();
        }
        return BookingDTOMapper.fromCollection(bookingViews);
    }

    @Override
    @Transactional
    public BookingOutDTO addBooking(Long bookerId, BookingDTO bookingDTO) {
        final User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new UserNotFoundException(bookerId));
        final Item item = itemRepository.findById(bookingDTO.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(bookingDTO.getItemId()));
        Booking booking = BookingDTOMapper.toBooking(bookingDTO, booker, item);
        this.checkIsAvailableItem(booking);
        this.checkBookerIsOwner(bookerId, booking);
        booking = bookingRepository.save(booking);
        log.info(BOOKING_ADDED, booking.getId());
        return BookingDTOMapper.fromBooking(booking);
    }

    @Override
    @Transactional
    public BookingOutDTO approvalBooking(Long ownerId, Long bookingId, boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        this.checkIsOwnerOfItem(ownerId, booking);
        this.checkBookingStatus(booking);
        booking.setApprovedStatus(isApproved);
        booking = bookingRepository.save(booking);
        log.info(BOOKING_APPROVED, booking.getId());
        return BookingDTOMapper.fromBooking(booking);
    }

    @Override
    public BookingOutDTO getBookingByOwnerOrBooker(Long userId, Long bookingId) {
        final Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        this.checkWhetherOwnerOrBooker(userId, booking);
        log.info(GET_BOOKING, booking.getId());
        return BookingDTOMapper.fromBooking(booking);
    }

    @Override
    public List<BookingOutDTO> getBookingsByBooker(Long bookerId, String stateParam, Integer from, Integer size) {
        this.checkUserExists(bookerId);
        log.info(GET_BOOKING_LIST_BY_BOOKER, bookerId);
        return this.getOutDTOListByStateParam(bookerId, stateParam, from, size);
    }

    @Override
    public List<BookingOutDTO> getBookingsByOwner(Long ownerId, String stateParam, Integer from, Integer size) {
        this.checkUserExists(ownerId);
        List<BookingOutDTO> bookings = this.getOutDTOListByStateParamToOwner(ownerId, stateParam, from, size);
        if (bookings.isEmpty()) {
            throw new OwnerHasNoItemsException(ownerId);
        }
        log.info(GET_BOOKING_LIST_BY_OWNER, ownerId);
        return bookings;
    }
}