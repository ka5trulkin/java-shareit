package ru.practicum.shareit.user.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserLogMessage {
    public static final String REQUEST_ADD_USER = "Запрос на добавление пользователя Email:'{}'";
    public static final String REQUEST_DELETE_USER = "Запрос на удаление пользователя ID:'{}'";
    public static final String REQUEST_UPDATE_USER = "Запрос на обновление пользователя ID:'{}'";
    public static final String REQUEST_GET_USER = "Запрос на получение пользователя ID:'{}'";
    public static final String REQUEST_GET_USER_LIST = "Запрос на получение списка пользователей";
}