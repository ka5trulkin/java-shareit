package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingInfoDTO;

import java.util.Collection;

@Setter
@Getter
public class ItemWithBookingInfoDTO extends ItemDTO {
    private BookingInfoDTO lastBooking;
    private BookingInfoDTO nextBooking;
    private Collection<CommentDTO> comments;

    public ItemWithBookingInfoDTO(Long id, Long ownerId, String name, String description, Boolean available,
                                  BookingInfoDTO lastBooking, BookingInfoDTO nextBooking) {
        super(id, ownerId, name, description, available);
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
    }

    public ItemWithBookingInfoDTO(Long id, Long ownerId, String name, String description, Boolean available,
                                  BookingInfoDTO lastBooking, BookingInfoDTO nextBooking, Collection<CommentDTO> comments) {
        super(id, ownerId, name, description, available);
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
        this.comments = comments;
    }
}