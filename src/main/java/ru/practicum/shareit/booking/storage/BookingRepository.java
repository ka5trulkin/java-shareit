package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.booking.model.Status.APPROVED;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner where b.booker.id = ?1")
    <T> List<T> findByBookerId(Long id, Sort sort, Class<T> type);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.booker.id = ?1 and b.start < ?2 and b.end > ?3")
    <T> List<T> findByBookerIdCurrentTime(Long id, LocalDateTime start, LocalDateTime end, Sort sort, Class<T> type);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.booker.id = ?1 and b.end < ?2")
    <T> List<T> findByBookerIdPastTime(Long id, LocalDateTime end, Sort sort, Class<T> type);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.booker.id = ?1 and b.start > ?2")
    <T> List<T> findByBookerIdFutureTime(Long id, LocalDateTime start, Sort sort, Class<T> type);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.booker.id = ?1 and b.status = ?2")
    <T> List<T> findByBookerIdAndStatus(Long id, Status status, Sort sort, Class<T> type);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.item.owner.id = ?1")
    <T> List<T> findByOwnerId(Long id, Sort sort, Class<T> type);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.item.owner.id = ?1 and b.start < ?2 and b.end > ?3")
    <T> List<T> findByOwnerIdCurrentTime(Long id, LocalDateTime start, LocalDateTime end, Sort sort, Class<T> type);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.item.owner.id = ?1 and b.end < ?2")
    <T> List<T> findByOwnerIdPastTime(Long id, LocalDateTime end, Sort sort, Class<T> type);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.item.owner.id = ?1 and b.start > ?2")
    <T> List<T> findByOwnerIdFutureTime(Long id, LocalDateTime start, Sort sort, Class<T> type);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.item.owner.id = ?1 and b.status = ?2")
    <T> List<T> findByOwnerIdAndStatus(Long id, Status status, Sort sort, Class<T> type);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.item.id in ?1 and b.item.owner.id = ?2")
    <T> List<T> findByOwnerItems(Collection<Long> itemId, Long ownerId, Class<T> type);

    <T> Optional<T> findTop1ByItem_IdAndItem_Owner_IdAndStartBeforeAndStatusOrderByEndDesc(Long itemId, Long ownerId,
                                                                                         LocalDateTime end, Status status,
                                                                                         Class<T> type);

    default <T> Optional<T> getLastBooking(Long itemId, Long ownerId, LocalDateTime end, Class<T> type) {
        return findTop1ByItem_IdAndItem_Owner_IdAndStartBeforeAndStatusOrderByEndDesc(itemId, ownerId, end, APPROVED, type);
    }

    <T> Optional<T> findTop1ByItem_IdAndItem_Owner_IdAndStartAfterAndStatusOrderByStart(Long itemId, Long ownerId,
                                                                                        LocalDateTime start, Status status,
                                                                                        Class<T> type);

    default <T> Optional<T> getNextBooking(Long itemId, Long ownerId, LocalDateTime start, Class<T> type) {
        return findTop1ByItem_IdAndItem_Owner_IdAndStartAfterAndStatusOrderByStart(itemId, ownerId, start, APPROVED, type);
    }

    Optional<BookingView> findFirstByBooker_IdAndItem_IdAndEndBeforeAndStatus(Long bookerId, Long itemId,
                                                                              LocalDateTime end, Status status);

    default Optional<BookingView> getByBookerIdAndItemId(Long bookerId, Long itemId, LocalDateTime end) {
        return findFirstByBooker_IdAndItem_IdAndEndBeforeAndStatus(bookerId, itemId, end, APPROVED);
    }
}