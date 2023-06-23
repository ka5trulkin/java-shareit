package ru.practicum.shareit.item.exception;

import ru.practicum.shareit.exception.NotFoundException;

import static ru.practicum.shareit.exception.ExceptionMessage.ITEM_NOT_FOUND;

public class ItemNotFoundException extends NotFoundException {
    public ItemNotFoundException(long id) {
        super(String.format(ITEM_NOT_FOUND, id));
    }
}