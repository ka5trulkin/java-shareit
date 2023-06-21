package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    private long id;
    private long ownerId;
    @NotBlank(message = "Поле Name не должно быть пустым")
    private String name;
    @NotBlank(message = "Поле Description не должно быть пустым")
    private String description;
    @NotNull(message = "Поле available не должно быть пустым")
    private boolean available;
}