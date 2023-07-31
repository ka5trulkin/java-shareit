package ru.practicum.shareit.exception.base;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessage {
    public static final String ITEM_NOT_FOUND = "Запрашиваемая вещь ID'%s' не найдена";
    public static final String ITEM_REQUEST_NOT_FOUND = "Запрос ID'%s' для поиска вещи не найден";
    public static final String OWNER_NOT_FOUND = "Владелец ID'%s' запрашиваемой вещи не найден";
    public static final String USER_NOT_FOUND = "Пользователь с ID:'%s' не найден";
    public static final String USER_EMAIL_ALREADY_EXIST = "Пользователь с email:'%s' уже существует";
    public static final String ITEM_IS_NOT_AVAILABLE = "'%s' сейчас не доступен(на)";
    public static final String BAD_BOOKING_TIME = "Указана некорректная дата бронирования";
    public static final String BAD_ITEM_UPDATE = "Все поля обновляемой вещи пустые";
    public static final String BAD_COMMENT_BY_AUTHOR_OR_ITEM = "Ошибка комментария. Пользователь или вещь не найдены";
    public static final String BOOKING_NOT_FOUND = "Бронирование ID:'%s' не найдено";
    public static final String NOT_OWNER_ITEM = "Пользователь ID:'%s' не является владельцем вещи ID:'%s'";
    public static final String BOOKER_IS_OWNER = "Пользователь ID:'%s' не может забронировать собственную вещь";
    public static final String OWNER_HAS_NO_ITEMS = "Вещи для пользователя ID'%s' не найдены";
    public static final String UNSUPPORTED_STATUS = "Unknown state: %s";
    public static final String STATUS_CANNOT_BE_CHANGED = "Данный статус:'%s' изменить нельзя";
}