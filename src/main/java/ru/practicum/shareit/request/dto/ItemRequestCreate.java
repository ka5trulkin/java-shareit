package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static ru.practicum.shareit.utils.validation.ValidationMessages.DESCRIPTION_SHOULD_NOT_BE_BLANK;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestCreate {
    @NotBlank(message = DESCRIPTION_SHOULD_NOT_BE_BLANK)
    private String description;
}