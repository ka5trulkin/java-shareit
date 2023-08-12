package ru.practicum.shareit.exception.user;

import ru.practicum.shareit.exception.base.NotFoundException;

import static ru.practicum.shareit.exception.base.ExceptionMessage.USER_NOT_FOUND;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(long id) {
        super(String.format(USER_NOT_FOUND, id));
    }
}