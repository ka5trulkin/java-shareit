package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
import ru.practicum.shareit.user.dto.UserIdDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

import static ru.practicum.shareit.booking.BookingLogMessage.*;
import static ru.practicum.shareit.booking.model.Status.REJECTED;
import static ru.practicum.shareit.booking.model.Status.WAITING;
import static ru.practicum.shareit.booking.service.UserType.BOOKER;
import static ru.practicum.shareit.booking.service.UserType.OWNER;

@Slf4j
@Service
@RequiredArgsConstructor
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
        if (Objects.equals(booking.getOwnerId(), userId)
                || Objects.equals(booking.getBookerId(), userId)) {
            return;
        }
        throw new BookingNotFoundException(booking.getId());
    }

    private void checkUserExists(Long userId) {
        userRepository.findById(userId, UserIdDTO.class).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private void checkBookingStatus(Booking booking) {
        if (booking.getStatus() != WAITING) {
            throw new StatusCannotBeChangedException(booking.getStatus());
        }
    }

    private Collection<BookingOutDTO> getOutDTOCollectionByStateParam(Long userId, String stateParam, UserType type) {
        final StateParam state;
        try {
            state = StateParam.valueOf(stateParam.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException(stateParam);
        }
        final Collection<BookingView> list;
        final LocalDateTime time = LocalDateTime.now();
        String bookingFieldStart = "start";
        final Sort sort = Sort.by(bookingFieldStart).descending();
        switch (type) {
            case BOOKER:
                switch (state) {
                    case ALL:
                        list = bookingRepository.findByBookerId(userId, sort, BookingView.class);
                        return BookingDTOMapper.fromCollection(list);
                    case CURRENT:
                        list = bookingRepository.findByBookerIdCurrentTime(userId, time, time, sort, BookingView.class);
                        return BookingDTOMapper.fromCollection(list);
                    case PAST:
                        list = bookingRepository.findByBookerIdPastTime(userId, time, sort, BookingView.class);
                        return BookingDTOMapper.fromCollection(list);
                    case FUTURE:
                        list = bookingRepository.findByBookerIdFutureTime(userId, time, sort, BookingView.class);
                        return BookingDTOMapper.fromCollection(list);
                    case WAITING:
                        list = bookingRepository.findByBookerIdAndStatus(userId, WAITING, sort, BookingView.class);
                        return BookingDTOMapper.fromCollection(list);
                    case REJECTED:
                        list = bookingRepository.findByBookerIdAndStatus(userId, REJECTED, sort, BookingView.class);
                        return BookingDTOMapper.fromCollection(list);
                }
            case OWNER:
                switch (state) {
                    case ALL:
                        list = bookingRepository.findByOwnerId(userId, sort, BookingView.class);
                        return BookingDTOMapper.fromCollection(list);
                    case CURRENT:
                        list = bookingRepository.findByOwnerIdCurrentTime(userId, time, time, sort, BookingView.class);
                        return BookingDTOMapper.fromCollection(list);
                    case PAST:
                        list = bookingRepository.findByOwnerIdPastTime(userId, time, sort, BookingView.class);
                        return BookingDTOMapper.fromCollection(list);
                    case FUTURE:
                        list = bookingRepository.findByOwnerIdFutureTime(userId, time, sort, BookingView.class);
                        return BookingDTOMapper.fromCollection(list);
                    case WAITING:
                        list = bookingRepository.findByOwnerIdAndStatus(userId, WAITING, sort, BookingView.class);
                        return BookingDTOMapper.fromCollection(list);
                    case REJECTED:
                        list = bookingRepository.findByOwnerIdAndStatus(userId, REJECTED, sort, BookingView.class);
                        return BookingDTOMapper.fromCollection(list);
                }
            default:
                throw new InternalError();
        }
    }

    @Override
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
        return this.getOutDTOCollectionByStateParam(bookerId, stateParam, BOOKER);
    }

    @Override
    public Collection<BookingOutDTO> getBookingsByOwner(Long ownerId, String stateParam) {
        this.checkUserExists(ownerId);
        Collection<BookingOutDTO> list = this.getOutDTOCollectionByStateParam(ownerId, stateParam, OWNER);
        if (list.isEmpty()) {
            throw new OwnerHasNoItemsException(ownerId);
        }
        log.info(GET_BOOKING_LIST_BY_OWNER, ownerId);
        return list;
    }
}