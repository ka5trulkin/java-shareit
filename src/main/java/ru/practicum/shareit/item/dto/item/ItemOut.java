package ru.practicum.shareit.item.dto.item;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingInfoDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTO;

import java.util.List;

@Setter
@Getter
public class ItemOut extends ItemDTO {
    private BookingInfoDTO lastBooking;
    private BookingInfoDTO nextBooking;
    private List<CommentDTO> comments;

    public ItemOut(Long id, Long ownerId, String name, String description, Boolean available, Long requestId,
                   BookingInfoDTO lastBooking, BookingInfoDTO nextBooking, List<CommentDTO> comments) {
        super(id, ownerId, name, description, available, requestId);
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
        this.comments = comments;
    }
}