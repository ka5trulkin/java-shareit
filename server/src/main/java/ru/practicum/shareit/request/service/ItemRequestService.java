package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestOutWithItems;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDTO addRequest(Long ownerId, ItemRequestCreate request);

    List<ItemRequestOutWithItems> getAllToOwner(Long ownerId);

    List<ItemRequestOutWithItems> getAll(Long userId, Integer from, Integer size);

    ItemRequestOutWithItems getRequest(Long userId, Long requestId);
}