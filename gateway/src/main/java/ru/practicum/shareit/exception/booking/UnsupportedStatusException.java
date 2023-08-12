package ru.practicum.shareit.exception.booking;

import ru.practicum.shareit.exception.base.RequestException;

import static ru.practicum.shareit.exception.base.ExceptionMessage.UNSUPPORTED_STATUS;

public class UnsupportedStatusException extends RequestException {
    public UnsupportedStatusException(String state) {
        super(String.format(UNSUPPORTED_STATUS, state));
    }
}