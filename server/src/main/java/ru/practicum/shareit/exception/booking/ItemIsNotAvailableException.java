package ru.practicum.shareit.exception.booking;

import ru.practicum.shareit.exception.base.RequestException;

import static ru.practicum.shareit.exception.base.ExceptionMessage.ITEM_IS_NOT_AVAILABLE;

public class ItemIsNotAvailableException extends RequestException {
    public ItemIsNotAvailableException(String itemName) {
        super(String.format(ITEM_IS_NOT_AVAILABLE, itemName));
    }
}