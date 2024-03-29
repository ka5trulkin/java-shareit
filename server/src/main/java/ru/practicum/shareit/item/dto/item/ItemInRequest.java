package ru.practicum.shareit.item.dto.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ItemInRequest {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}