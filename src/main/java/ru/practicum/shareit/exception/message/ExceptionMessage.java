package ru.practicum.shareit.exception.message;

public enum ExceptionMessage {
    OBJECT_ALREADY_EXIST("Объект ID:'%s' уже существует"),
    OBJECT_BY_ID_NOT_FOUND("Объект ID:'%s' не найден"),
    OBJECT_NOT_FOUND("Объект не найден"),
    USER_NOT_FOUND("Пользователь с ID:'%s' не найден"),
    USER_EMAIL_ALREADY_EXIST("Пользователь с email:'%s' уже существует");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}