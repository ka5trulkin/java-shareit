package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.item.ItemInRequest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ItemRequestOutWithItems extends ItemRequestDTO {
    private List<ItemInRequest> items;

    public ItemRequestOutWithItems(Long id, String description, LocalDateTime created, List<ItemInRequest> items) {
        super(id, description, created);
        this.items = items;
    }
}