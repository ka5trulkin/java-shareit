package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.item.ItemIdAndName;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserIdDTO;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingDTOMapper {

    public Booking toBooking(BookingDTO bookingDTO, User booker, Item item) {
        return Booking.builder()
                .start(bookingDTO.getStart())
                .end(bookingDTO.getEnd())
                .booker(booker)
                .item(item)
                .status(Status.WAITING)
                .build();
    }

    public BookingOutDTO fromViewToOut(BookingView view) {
        return BookingOutDTO.builder()
                .id(view.getId())
                .start(view.getStart())
                .end(view.getEnd())
                .status(view.getStatus())
                .booker(new UserIdDTO(view.getBooker().getId()))
                .item(new ItemIdAndName(view.getItem().getId(), view.getItem().getName()))
                .build();
    }

    public BookingInfoDTO fromViewToInfo(BookingView view) {
        return BookingInfoDTO.builder()
                .id(view.getId())
                .bookerId(view.getBooker().getId())
                .start(view.getStart())
                .end(view.getEnd())
                .build();
    }

    public BookingOutDTO fromBooking(Booking booking) {
        return BookingOutDTO.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(new UserIdDTO(booking.getBookerId()))
                .item(new ItemIdAndName(booking.getItemId(), booking.getItemName()))
                .build();
    }

    public List<BookingOutDTO> fromCollection(Collection<BookingView> booking) {
        return booking.stream()
                .map(BookingDTOMapper::fromViewToOut)
                .collect(Collectors.toList());
    }
}