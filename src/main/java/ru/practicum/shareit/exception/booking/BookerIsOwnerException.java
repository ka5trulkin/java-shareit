package ru.practicum.shareit.exception.booking;

import ru.practicum.shareit.exception.base.NotFoundException;

import static ru.practicum.shareit.exception.base.ExceptionMessage.BOOKER_IS_OWNER;

public class BookerIsOwnerException extends NotFoundException {
    public BookerIsOwnerException(Long bookerId) {
        super(String.format(BOOKER_IS_OWNER, bookerId));
    }
}