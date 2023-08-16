package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.item.ItemInRequest;
import ru.practicum.shareit.item.dto.item.ItemViewWithRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestDTOMapper {
    public ItemRequest toItemRequestOnCreate(Long ownerId, ItemRequestCreate request) {
        final User user = User.builder().id(ownerId).build();
        return ItemRequest.builder()
                .description(request.getDescription())
                .owner(user)
                .build();
    }

    public ItemRequestDTO fromItemRequest(ItemRequest request) {
        return ItemRequestDTO.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
    }

    public ItemInRequest fromItemViewWithRequest(ItemViewWithRequest item) {
        return ItemInRequest.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .requestId(item.getRequest().getId())
                .build();
    }

    public ItemRequestOutWithItems getOutWithItems(ItemRequestDTO request,
                                                   Collection<ItemViewWithRequest> items) {
        return new ItemRequestOutWithItems(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                items.stream()
                        .map(ItemRequestDTOMapper::fromItemViewWithRequest)
                        .collect(Collectors.toList()));
    }

    public ItemRequestOutWithItems getOutWithItems(ItemRequest request, Collection<ItemViewWithRequest> items) {
        return getOutWithItems(fromItemRequest(request), items);
    }

    public List<ItemRequestOutWithItems> getOutWithItems(Collection<ItemRequestDTO> requests,
                                                         Collection<ItemViewWithRequest> items) {
        return requests.stream()
                .map(request -> getOutWithItems(request,
                        items.stream()
                                .filter(item -> Objects.equals(request.getId(), item.getRequest().getId()))
                                .collect(Collectors.toList())))
                .sorted(Comparator.comparing(ItemRequestDTO::getCreated, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}