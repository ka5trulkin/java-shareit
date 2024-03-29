package ru.practicum.shareit.utils.validation;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationMessages {
    public static final String NAME_SHOULD_NOT_BE_BLANK = "Поле Name не должно быть пустым";
    public static final String TEXT_SHOULD_NOT_BE_BLANK = "Поле Text не должно быть пустым";
    public static final String DESCRIPTION_SHOULD_NOT_BE_BLANK = "Поле Description не должно быть пустым";
    public static final String AVAILABLE_SHOULD_NOT_BE_BLANK = "Поле Available не должно быть пустым";
    public static final String BAD_EMAIL = "Поле Email не соответствует формату электронной почты";
    public static final String EMAIL_SHOULD_NOT_BE_BLANK = "Поле Email не должно быть пустым";
    public static final String START_SHOULD_NOT_BE_BLANK = "Поле Start не должно быть пустым";
    public static final String END_SHOULD_NOT_BE_BLANK = "Поле End не должно быть пустым";
    public static final String TIME_SHOULD_NOT_BE_PAST = "Указанное время не должно быть в прошлом";
}