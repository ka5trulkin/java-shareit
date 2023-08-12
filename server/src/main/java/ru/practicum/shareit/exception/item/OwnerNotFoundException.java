package ru.practicum.shareit.exception.item;

import ru.practicum.shareit.exception.base.NotFoundException;

import static ru.practicum.shareit.exception.base.ExceptionMessage.OWNER_NOT_FOUND;

public class OwnerNotFoundException extends NotFoundException {
    public OwnerNotFoundException(long id) {
        super(String.format(OWNER_NOT_FOUND, id));
    }
}