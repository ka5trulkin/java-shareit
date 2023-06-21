package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class User {
    private long id;
    @NotBlank(message = "Поле Name не должно быть пустым")
    private String name;
    @Email(message = "Поле Email не соответствует формату электронной почты")
    @NotBlank(message = "Поле Email не должно быть пустым")
    private String email;
}