package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserLogMessage {
    public static final String REQUEST_ADD_USER = "Запрос на добавление пользователя Email:'{}'";
    public static final String REQUEST_DELETE_USER = "Запрос на удаление пользователя ID:'{}'";
    public static final String REQUEST_UPDATE_USER = "Запрос на обновление пользователя ID:'{}'";
    public static final String REQUEST_GET_USER = "Запрос на получение пользователя ID:'{}'";
    public static final String REQUEST_GET_USER_LIST = "Запрос на получение списка пользователей";
    public static final String USER_ADDED = "Пользователь ID:'{}'; Email:'{}' добавлен";
    public static final String USER_UPDATED = "Пользователь ID:'{}' обновлен";
    public static final String USER_DELETED = "Пользователь ID:'{}' удален";
    public static final String GET_USER = "Получение пользователя ID:'{}'";
    public static final String GET_USER_LIST = "Получение списка пользователей";
}