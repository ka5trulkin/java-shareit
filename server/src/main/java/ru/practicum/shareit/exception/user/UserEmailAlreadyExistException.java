package ru.practicum.shareit.exception.user;

import ru.practicum.shareit.exception.base.DataConflictException;

import static ru.practicum.shareit.exception.base.ExceptionMessage.USER_EMAIL_ALREADY_EXIST;

public class UserEmailAlreadyExistException extends DataConflictException {
    public UserEmailAlreadyExistException(String email) {
        super(String.format(USER_EMAIL_ALREADY_EXIST, email));
    }
}