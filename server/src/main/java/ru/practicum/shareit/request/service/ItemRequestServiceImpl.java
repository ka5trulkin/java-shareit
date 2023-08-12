package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.item_request.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.user.UserNotFoundException;
import ru.practicum.shareit.item.dto.item.ItemViewWithRequest;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTOMapper;
import ru.practicum.shareit.request.dto.ItemRequestOutWithItems;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.utils.PageApp;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.request.ItemRequestMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    private List<Long> getIds(Collection<ItemRequestDTO> requests) {
        return requests.stream()
                .map(ItemRequestDTO::getId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemRequestDTO addRequest(Long ownerId, ItemRequestCreate request) {
        this.checkUserExists(ownerId);
        ItemRequest itemRequest = ItemRequestDTOMapper.toItemRequestOnCreate(ownerId, request);
        itemRequest = itemRequestRepository.save(itemRequest);
        log.info(ITEM_REQUEST_ADDED, ownerId);
        return ItemRequestDTOMapper.fromItemRequest(itemRequest);
    }

    @Override
    public List<ItemRequestOutWithItems> getAllToOwner(Long ownerId) {
        this.checkUserExists(ownerId);
        final List<ItemRequestDTO> requests = itemRequestRepository.findByOwner_Id(ownerId);
        final List<ItemViewWithRequest> items = itemRepository.findWithRequestsByOwnerId(ownerId);
        log.info(GET_REQUESTS_BY_OWNER, ownerId);
        return ItemRequestDTOMapper.getOutWithItems(requests, items);
    }

    @Override
    public List<ItemRequestOutWithItems> getAll(Long userId, Integer from, Integer size) {
        this.checkUserExists(userId);
        final List<ItemRequestDTO> requests =
                itemRequestRepository.findByOwner_IdNot(userId, PageApp.ofStartingIndex(from, size));
        final List<ItemViewWithRequest> items = itemRepository.findByRequestIdIn(this.getIds(requests));
        log.info(GET_REQUESTS_BY_USER, userId);
        return ItemRequestDTOMapper.getOutWithItems(requests, items);
    }

    @Override
    public ItemRequestOutWithItems getRequest(Long userId, Long requestId) {
        this.checkUserExists(userId);
        final ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ItemRequestNotFoundException(requestId));
        final List<ItemViewWithRequest> items = itemRepository.findByRequestId(requestId);
        log.info(GET_REQUEST, userId);
        return ItemRequestDTOMapper.getOutWithItems(request, items);
    }
}