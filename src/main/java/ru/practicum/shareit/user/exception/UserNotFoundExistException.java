package ru.practicum.shareit.user.exception;

import ru.practicum.shareit.exception.NotFoundException;

import static ru.practicum.shareit.exception.ExceptionMessage.USER_NOT_FOUND;

public class UserNotFoundExistException extends NotFoundException {
    public UserNotFoundExistException(long id) {
        super(String.format(USER_NOT_FOUND, id));
    }
}