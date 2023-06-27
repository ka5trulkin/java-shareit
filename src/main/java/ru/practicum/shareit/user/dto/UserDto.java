package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.service.CreateInfo;
import ru.practicum.shareit.service.UpdateInfo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static ru.practicum.shareit.service.MessagesApp.*;

@Data
@Builder
public class UserDto {
    private Long id;
    @NotBlank(groups = CreateInfo.class, message = NAME_SHOULD_NOT_BE_BLANK)
    private String name;
    @Email(groups = {CreateInfo.class, UpdateInfo.class}, message = BAD_EMAIL)
    @NotBlank(groups = CreateInfo.class, message = EMAIL_SHOULD_NOT_BE_BLANK)
    private String email;
}