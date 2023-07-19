package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.exception.booking.UnsupportedStatusException;

public enum StateParam {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static StateParam stateOf(String value) {
        final StateParam state;
        try {
            state = StateParam.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException(value);
        }
        return state;
    }
}