package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@Builder
public class UserDto {
    private String name;
    @Email(message = "Поле Email не соответствует формату электронной почты")
    private String email;
}