package ru.practicum.shareit.exception;

public enum ExceptionMessage {
    ITEM_NOT_FOUND("Запрашиваемая вещь не найдена"),
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