package ru.practicum.shareit.item;

public enum ItemLogMessage {
    REQUEST_ADD_ITEM("Запрос на добавление вещи Name:'{}'"),
    REQUEST_UPDATE_ITEM("Запрос на обновление вещи ID:'{}'"),
    REQUEST_GET_ITEM("Запрос на получение вещи ID:'{}'"),
    REQUEST_GET_ITEM_BY_QUERY("Запрос на получение вещи:'{}'"),
    REQUEST_GET_ITEM_LIST("Запрос на получение списка вещей"),
    ITEM_ADDED("Вещь ID:'{}', Name:'{}' добавлена"),
    ITEM_UPDATED("Вещь ID:'{}' обновлена"),
    GET_ITEM("Получение вещи ID:'{}'"),
    GET_ITEM_BY_QUERY("Получение вещи по запросу:'{}'"),
    GET_ITEM_LIST("Получение списка вещей");

    private final String message;

    ItemLogMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}