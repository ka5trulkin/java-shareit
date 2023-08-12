package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.utils.CreateInfo;
import ru.practicum.shareit.utils.UpdateInfo;
import ru.practicum.shareit.utils.validation.ItemUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static ru.practicum.shareit.utils.validation.ValidationMessages.*;

@Builder
@Data
@AllArgsConstructor
@ItemUpdate(groups = UpdateInfo.class)
public class ItemDTO {
    private Long id;
    private Long ownerId;
    @NotBlank(groups = CreateInfo.class, message = NAME_SHOULD_NOT_BE_BLANK)
    private String name;
    @NotBlank(groups = CreateInfo.class, message = DESCRIPTION_SHOULD_NOT_BE_BLANK)
    private String description;
    @NotNull(groups = CreateInfo.class, message = AVAILABLE_SHOULD_NOT_BE_BLANK)
    private Boolean available;
    private Long requestId;
}