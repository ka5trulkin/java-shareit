package ru.practicum.shareit.exception.item_request;

import ru.practicum.shareit.exception.base.NotFoundException;

import static ru.practicum.shareit.exception.base.ExceptionMessage.ITEM_REQUEST_NOT_FOUND;

public class ItemRequestNotFoundException extends NotFoundException {
    public ItemRequestNotFoundException(long requestId) {
        super(String.format(ITEM_REQUEST_NOT_FOUND, requestId));
    }
}