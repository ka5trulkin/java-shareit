package ru.practicum.shareit.item.exception;

import ru.practicum.shareit.exception.NotFoundException;

import static ru.practicum.shareit.exception.ExceptionMessage.OWNER_NOT_FOUND;

public class OwnerNotFoundException extends NotFoundException {
    public OwnerNotFoundException(long id) {
        super(String.format(OWNER_NOT_FOUND, id));
    }
}