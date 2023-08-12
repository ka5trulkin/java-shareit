package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.utils.CreateInfo;
import ru.practicum.shareit.utils.UpdateInfo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static ru.practicum.shareit.utils.validation.ValidationMessages.*;

@Data
@Builder
@AllArgsConstructor
public class UserDTO {
    private Long id;
    @NotBlank(groups = CreateInfo.class, message = NAME_SHOULD_NOT_BE_BLANK)
    private String name;
    @Email(groups = {CreateInfo.class, UpdateInfo.class}, message = BAD_EMAIL)
    @NotBlank(groups = CreateInfo.class, message = EMAIL_SHOULD_NOT_BE_BLANK)
    private String email;
}