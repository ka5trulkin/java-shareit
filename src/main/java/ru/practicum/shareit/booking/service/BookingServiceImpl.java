package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingDTOMapper;
import ru.practicum.shareit.booking.dto.BookingOutDTO;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.booking.*;
import ru.practicum.shareit.exception.item.ItemNotFoundException;
import ru.practicum.shareit.exception.user.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
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

    private void checkIsAvailable(Booking booking) {
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
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private void checkBookingStatus(Booking booking) {
        if (booking.getStatus() != WAITING) {
            throw new StatusCannotBeChangedException(booking.getStatus());
        }
    }

    private Collection<BookingOutDTO> getOutDTOListByStateParam(Long userId, String stateParam) {
        final StateParam state = StateParam.stateOf(stateParam);
        final Collection<BookingView> bookingViews;
        final LocalDateTime time = LocalDateTime.now();
        switch (state) {
            case ALL:
                bookingViews = bookingRepository.findByBookerId(userId);
                break;
            case CURRENT:
                bookingViews = bookingRepository.findByBookerIdCurrentTime(userId, time, time);
                break;
            case PAST:
                bookingViews = bookingRepository.findByBookerIdPastTime(userId, time);
                break;
            case FUTURE:
                bookingViews = bookingRepository.findByBookerIdFutureTime(userId, time);
                break;
            case WAITING:
                bookingViews = bookingRepository.findByBookerIdAndStatus(userId, WAITING);
                break;
            case REJECTED:
                bookingViews = bookingRepository.findByBookerIdAndStatus(userId, REJECTED);
                break;
            default:
                throw new InternalError();
        }
        return BookingDTOMapper.fromCollection(bookingViews);
    }

    private Collection<BookingOutDTO> getOutDTOListByStateParamToOwner(Long userId, String stateParam) {
        final StateParam state = StateParam.stateOf(stateParam);
        final Collection<BookingView> bookingViews;
        final LocalDateTime time = LocalDateTime.now();
        switch (state) {
            case ALL:
                bookingViews = bookingRepository.findByOwnerId(userId);
                break;
            case CURRENT:
                bookingViews = bookingRepository.findByOwnerIdCurrentTime(userId, time, time);
                break;
            case PAST:
                bookingViews = bookingRepository.findByOwnerIdPastTime(userId, time);
                break;
            case FUTURE:
                bookingViews = bookingRepository.findByOwnerIdFutureTime(userId, time);
                break;
            case WAITING:
                bookingViews = bookingRepository.findByOwnerIdAndStatus(userId, WAITING);
                break;
            case REJECTED:
                bookingViews = bookingRepository.findByOwnerIdAndStatus(userId, REJECTED);
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
        final Booking booking = BookingDTOMapper.toBooking(bookingDTO, booker, item);
        this.checkIsAvailable(booking);
        this.checkBookerIsOwner(bookerId, booking);
        bookingRepository.save(booking);
        log.info(BOOKING_ADDED, booking.getId());
        return BookingDTOMapper.fromBooking(booking);
    }

    @Override
    @Transactional
    public BookingOutDTO approvalBooking(Long ownerId, Long bookingId, boolean isApproved) {
        final Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        this.checkIsOwnerOfItem(ownerId, booking);
        this.checkBookingStatus(booking);
        booking.setApprovedStatus(isApproved);
        bookingRepository.save(booking);
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
    public Collection<BookingOutDTO> getBookingsByBooker(Long bookerId, String stateParam) {
        this.checkUserExists(bookerId);
        log.info(GET_BOOKING_LIST_BY_BOOKER, bookerId);
        return this.getOutDTOListByStateParam(bookerId, stateParam);
    }

    @Override
    public Collection<BookingOutDTO> getBookingsByOwner(Long ownerId, String stateParam) {
        this.checkUserExists(ownerId);
        Collection<BookingOutDTO> list = this.getOutDTOListByStateParamToOwner(ownerId, stateParam);
        if (list.isEmpty()) {
            throw new OwnerHasNoItemsException(ownerId);
        }
        log.info(GET_BOOKING_LIST_BY_OWNER, ownerId);
        return list;
    }
}