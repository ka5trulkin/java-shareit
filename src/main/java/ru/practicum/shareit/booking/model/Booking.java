package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.shareit.booking.model.Status.APPROVED;
import static ru.practicum.shareit.booking.model.Status.REJECTED;

@Entity
@Table(name = "bookings")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "starting")
    private LocalDateTime start;
    @Column(name = "ending")
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    private User booker;
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    public Long getBookerId() {
        return booker.getId();
    }

    public Long getItemId() {
        return item.getId();
    }

    public String getItemName() {
        return item.getName();
    }

    public Long getOwnerId() {
        return item.getOwner().getId();
    }

    public void setApprovedStatus(boolean isApproved) {
        if (isApproved) {
            this.setStatus(APPROVED);
        } else {
            this.setStatus(REJECTED);
        }
    }
}