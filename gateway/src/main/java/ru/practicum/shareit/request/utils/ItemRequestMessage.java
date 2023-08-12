package ru.practicum.shareit.request.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ItemRequestMessage {
    public static final String REQUEST_ADD_ITEM_REQUEST = "Запрос на добавление запроса для поиска вещи от пользователя ID:'{}'";
    public static final String REQUEST_REQUESTS_BY_OWNER = "Запрос на получение списка запросов для владельца ID:'{}";
    public static final String REQUEST_REQUESTS_BY_USER = "Запрос на получение списка запросов для пользователя ID:'{}";
    public static final String REQUEST_GET_REQUEST = "Запрос на получение запроса ID:{} на поиск вещи";
}