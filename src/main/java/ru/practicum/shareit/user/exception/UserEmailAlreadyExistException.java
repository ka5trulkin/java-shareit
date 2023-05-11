package ru.practicum.shareit.user.exception;

import ru.practicum.shareit.exception.DataConflictException;

import static ru.practicum.shareit.exception.message.ExceptionMessage.USER_EMAIL_ALREADY_EXIST;

public class UserEmailAlreadyExistException extends DataConflictException {
    public UserEmailAlreadyExistException(String email) {
        super(String.format(USER_EMAIL_ALREADY_EXIST.message(), email));
    }
}