package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ItemLogMessage {
    public static final String REQUEST_ADD_ITEM = "Запрос на добавление вещи Name:'{}'";
    public static final String REQUEST_ADD_COMMENT = "Запрос на добавление комментария от пользователя ID:'{}'";
    public static final String REQUEST_UPDATE_ITEM = "Запрос на обновление вещи ID:'{}'";
    public static final String REQUEST_GET_ITEM = "Запрос на получение вещи ID:'{}'";
    public static final String REQUEST_GET_ITEM_BY_QUERY = "Запрос на получение вещи:'{}'";
    public static final String REQUEST_GET_ITEM_LIST = "Запрос на получение списка вещей";
    public static final String ITEM_ADDED = "Вещь ID:'{}'; Name:'{}' добавлена";
    public static final String COMMENT_ADDED = "Комментарий ID:'{}' добавлен";
    public static final String ITEM_UPDATED = "Вещь ID:'{}' обновлена";
    public static final String GET_ITEM = "Получение вещи ID:'{}'";
    public static final String GET_ITEM_LIST_BY_QUERY = "Получение списка вещей по запросу:'{}'";
    public static final String GET_ITEM_LIST = "Получение списка вещей";
}