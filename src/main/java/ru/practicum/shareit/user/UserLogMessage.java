package ru.practicum.shareit.user;

public enum UserLogMessage {
    REQUEST_ADD_USER("Запрос на добавление пользователя Email:'{}'"),
    REQUEST_DELETE_USER("Запрос на удаление пользователя ID:'{}'"),
    REQUEST_UPDATE_USER("Запрос на обновление пользователя ID:'{}'"),
    REQUEST_GET_USER("Запрос на получение пользователя ID:'{}'"),
    REQUEST_GET_USER_LIST("Запрос на получение списка пользователей"),
    USER_ADDED("Пользователь ID:'{}', Email:'{}' добавлен"),
    USER_UPDATED("Пользователь ID:'{}' обновлен"),
    USER_DELETED("Пользователь ID:'{}' удален"),
    GET_USER("Получение пользователя ID:'{}'"),
    GET_USER_LIST("Получение списка пользователей");

    private final String message;

    UserLogMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}