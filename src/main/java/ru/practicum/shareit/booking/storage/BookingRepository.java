package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.booking.model.Status.APPROVED;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.booker.id = :id order by b.start DESC")
    List<BookingView> findByBookerId(@Param("id") Long id, Pageable pageable);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.booker.id = :id and b.start < :time and b.end > :time order by b.start DESC")
    List<BookingView> findByBookerIdCurrentTime(@Param("id") Long id, @Param("time") LocalDateTime time, Pageable pageable);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.booker.id = :id and b.end < :end order by b.start DESC")
    List<BookingView> findByBookerIdPastTime(@Param("id") Long id, @Param("end") LocalDateTime end, Pageable pageable);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.booker.id = :id and b.start > :start order by b.start DESC")
    List<BookingView> findByBookerIdFutureTime(@Param("id") Long id, @Param("start") LocalDateTime start, Pageable pageable);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.booker.id = :id and b.status = :status order by b.start DESC")
    List<BookingView> findByBookerIdAndStatus(@Param("id") Long id, @Param("status") Status status, Pageable pageable);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.item.owner.id = :id order by b.start DESC")
    List<BookingView> findByOwnerId(@Param("id") Long id, Pageable pageable);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.item.owner.id = :id and b.start < :time and b.end > :time order by b.start DESC")
    List<BookingView> findByOwnerIdCurrentTime(@Param("id") Long id, @Param("time") LocalDateTime time, Pageable pageable);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.item.owner.id = :id and b.end < :end order by b.start DESC")
    List<BookingView> findByOwnerIdPastTime(@Param("id") Long id, @Param("end") LocalDateTime end, Pageable pageable);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.item.owner.id = :id and b.start > :start order by b.start DESC")
    List<BookingView> findByOwnerIdFutureTime(@Param("id") Long id, @Param("start") LocalDateTime start, Pageable pageable);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner " +
            "where b.item.owner.id = :id and b.status = :status order by b.start DESC")
    List<BookingView> findByOwnerIdAndStatus(@Param("id") Long id, @Param("status") Status status, Pageable pageable);

    @Query("select b from Booking b join fetch b.booker join fetch b.item i join fetch i.owner where b.item.id in :ids")
    List<BookingView> findByItemIdIn(@Param("ids") Collection<Long> ids);

    Optional<BookingView> findTop1ByItem_IdAndItem_Owner_IdAndStartBeforeAndStatusOrderByEndDesc(Long itemId, Long ownerId,
                                                                                                 LocalDateTime end, Status status);

    default Optional<BookingView> getLastBooking(Long itemId, Long ownerId, LocalDateTime end) {
        return findTop1ByItem_IdAndItem_Owner_IdAndStartBeforeAndStatusOrderByEndDesc(itemId, ownerId, end, APPROVED);
    }

    Optional<BookingView> findTop1ByItem_IdAndItem_Owner_IdAndStartAfterAndStatusOrderByStart(Long itemId, Long ownerId,
                                                                                              LocalDateTime start, Status status);

    default Optional<BookingView> getNextBooking(Long itemId, Long ownerId, LocalDateTime start) {
        return findTop1ByItem_IdAndItem_Owner_IdAndStartAfterAndStatusOrderByStart(itemId, ownerId, start, APPROVED);
    }

    Optional<BookingView> findFirstByBooker_IdAndItem_IdAndEndBeforeAndStatus(Long bookerId, Long itemId,
                                                                              LocalDateTime end, Status status);

    default Optional<BookingView> getByBookerIdAndItemId(Long bookerId, Long itemId, LocalDateTime end) {
        return findFirstByBooker_IdAndItem_IdAndEndBeforeAndStatus(bookerId, itemId, end, APPROVED);
    }
}