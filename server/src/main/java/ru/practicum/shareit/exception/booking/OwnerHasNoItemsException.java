package ru.practicum.shareit.exception.booking;

import ru.practicum.shareit.exception.base.RequestException;

import static ru.practicum.shareit.exception.base.ExceptionMessage.OWNER_HAS_NO_ITEMS;

public class OwnerHasNoItemsException extends RequestException {
    public OwnerHasNoItemsException(Long ownerId) {
        super(String.format(OWNER_HAS_NO_ITEMS, ownerId));
    }
}