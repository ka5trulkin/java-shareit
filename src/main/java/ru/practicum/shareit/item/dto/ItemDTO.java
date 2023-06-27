package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.service.CreateInfo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static ru.practicum.shareit.service.MessagesApp.*;

@Builder
@Data
@AllArgsConstructor
public class ItemDTO {
    private Long id;
    private Long ownerId;
    @NotBlank(groups = CreateInfo.class, message = NAME_SHOULD_NOT_BE_BLANK)
    private String name;
    @NotBlank(groups = CreateInfo.class, message = DESCRIPTION_SHOULD_NOT_BE_BLANK)
    private String description;
    @NotNull(groups = CreateInfo.class, message = AVAILABLE_SHOULD_NOT_BE_BLANK)
    private Boolean available;
}