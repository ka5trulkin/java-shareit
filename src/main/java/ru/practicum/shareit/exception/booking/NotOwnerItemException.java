package ru.practicum.shareit.exception.booking;

import ru.practicum.shareit.exception.base.NotFoundException;

import static ru.practicum.shareit.exception.base.ExceptionMessage.NOT_OWNER_ITEM;

public class NotOwnerItemException extends NotFoundException {
    public NotOwnerItemException(Long ownerId, Long itemId) {
        super(String.format(NOT_OWNER_ITEM, ownerId, itemId));
    }
}