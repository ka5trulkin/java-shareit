package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@Data
public class ItemDto {
    private long id;
    @NotBlank(groups = CreateInfo.class, message = "Поле Name не должно быть пустым")
    private String name;
    @NotBlank(groups = CreateInfo.class, message = "Поле Description не должно быть пустым")
    private String description;
    @NotNull(groups = CreateInfo.class, message = "Поле available не должно быть пустым")
    private Boolean available;
}