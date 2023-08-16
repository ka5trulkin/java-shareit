package ru.practicum.shareit.exception.base;

public class DataConflictException extends RuntimeException {
    public DataConflictException() {
    }

    public DataConflictException(String message) {
        super(message);
    }
}