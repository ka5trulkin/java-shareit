package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.item.ItemView;
import ru.practicum.shareit.user.dto.UserView;

import java.time.LocalDateTime;

public interface BookingView {
    Long getId();

    LocalDateTime getStart();

    LocalDateTime getEnd();

    Status getStatus();

    UserView getBooker();

    ItemView getItem();

    void setId(Long id);

    void setStart(LocalDateTime start);

    void setEnd(LocalDateTime end);

    void setStatus(Status status);

    void setBooker(UserView booker);

    void setItem(ItemView item);
}