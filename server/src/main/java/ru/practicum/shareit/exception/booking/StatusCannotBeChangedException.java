package ru.practicum.shareit.exception.booking;

import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.base.RequestException;

import static ru.practicum.shareit.exception.base.ExceptionMessage.STATUS_CANNOT_BE_CHANGED;

public class StatusCannotBeChangedException extends RequestException {
    public StatusCannotBeChangedException(Status status) {
        super(String.format(STATUS_CANNOT_BE_CHANGED, status));
    }
}