package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemView;
import ru.practicum.shareit.user.dto.UserView;

import java.time.LocalDateTime;

public interface BookingView {
    Long getId();

    LocalDateTime getStart();

    LocalDateTime getEnd();

    Status getStatus();

    UserView getBooker();

    ItemView getItem();
}