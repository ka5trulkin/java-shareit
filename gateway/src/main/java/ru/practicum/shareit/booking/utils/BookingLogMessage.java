package ru.practicum.shareit.booking.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BookingLogMessage {
    public static final String REQUEST_ADD_BOOKING = "Запрос на бронирование вещи ID:'{}'";
    public static final String REQUEST_APPROVAL_BOOKING = "Запрос на изменение статуса бронирования ID:'{}'";
    public static final String REQUEST_GET_BOOKING = "Запрос на получение бронирования ID:'{}'";
    public static final String REQUEST_GET_BOOKING_LIST_BY_BOOKER = "Запрос на получение списка бронирования пользователя ID:'{}'";
    public static final String REQUEST_GET_BOOKING_LIST_BY_OWNER = "Запрос на получение списка бронирования владельца ID:'{}'";
}