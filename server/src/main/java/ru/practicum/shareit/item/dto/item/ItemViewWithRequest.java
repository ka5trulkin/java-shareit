package ru.practicum.shareit.item.dto.item;

import ru.practicum.shareit.request.dto.ItemRequestView;

public interface ItemViewWithRequest {
    Long getId();

    String getName();

    String getDescription();

    boolean isAvailable();

    ItemRequestView getRequest();

    void setId(Long id);

    void setName(String name);

    void setDescription(String description);

    void setAvailable(boolean available);

    void setRequest(ItemRequestView request);
}