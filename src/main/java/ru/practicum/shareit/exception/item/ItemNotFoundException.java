package ru.practicum.shareit.exception.item;

import ru.practicum.shareit.exception.base.NotFoundException;

import static ru.practicum.shareit.exception.base.ExceptionMessage.ITEM_NOT_FOUND;

public class ItemNotFoundException extends NotFoundException {
    public ItemNotFoundException(long id) {
        super(String.format(ITEM_NOT_FOUND, id));
    }
}