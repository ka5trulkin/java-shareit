package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.AbstractTest;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest extends AbstractTest {
    @Mock
    private ItemRequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestServiceImpl service;

    @Test
    void addRequest() {
        final ItemRequest itemRequest = getItemRequest();
        final ItemRequestDTO expectedDto = ItemRequestDTOMapper.fromItemRequest(itemRequest);

        when(userRepository.existsById(idOne)).thenReturn(true);
        when(requestRepository.save(any())).thenReturn(itemRequest);

        assertThat(service.addRequest(idOne, new ItemRequestCreate())).isEqualTo(expectedDto);

        final InOrder inOrder = inOrder(userRepository, requestRepository);
        inOrder.verify(userRepository).existsById(any());
        inOrder.verify(requestRepository).save(any());
    }

    @Test
    void addRequest_whenUserNotExists_thenThrowUserNotFoundException() {
        when(userRepository.existsById(idOne)).thenReturn(false);

        assertThatThrownBy(() -> service.addRequest(idOne, new ItemRequestCreate()))
                .isInstanceOf(UserNotFoundException.class);

        verify(requestRepository, never()).save(any());
    }

    @Test
    void getAllToOwner() {
        final List<ItemRequestDTO> itemRequestDto = List.of(getItemRequestDto());
        final List<ItemViewWithRequest> itemViewWithRequest = List.of(getItemViewWithRequest());
        final List<ItemRequestOutWithItems> expectedDto = ItemRequestDTOMapper.getOutWithItems(itemRequestDto, itemViewWithRequest);

        when(userRepository.existsById(any())).thenReturn(true);
        when(requestRepository.findByOwner_Id(any())).thenReturn(itemRequestDto);
        when(itemRepository.findWithRequestsByOwnerId(any())).thenReturn(itemViewWithRequest);

        assertThat(service.getAllToOwner(idOne)).hasSize(1);

        final InOrder inOrder = inOrder(userRepository, requestRepository, itemRepository);
        inOrder.verify(userRepository).existsById(any());
        inOrder.verify(requestRepository).findByOwner_Id(any());
        inOrder.verify(itemRepository).findWithRequestsByOwnerId(any());

        assertThat(service.getAllToOwner(idOne)).isEqualTo(expectedDto);
    }

    @Test
    void getAllToOwner_whenUserNotExists_thenThrowUserNotFoundException() {
        when(userRepository.existsById(idOne)).thenReturn(false);

        assertThatThrownBy(() -> service.getAllToOwner(idOne))
                .isInstanceOf(UserNotFoundException.class);

        verify(requestRepository, never()).findByOwner_Id(any());
        verify(itemRepository, never()).findWithRequestsByOwnerId(any());
    }

    @Test
    void getAll() {
        final List<ItemRequestDTO> itemRequestDto = List.of(getItemRequestDto());
        final List<ItemViewWithRequest> itemViewWithRequest = List.of(getItemViewWithRequest());
        final List<ItemRequestOutWithItems> expectedDto = ItemRequestDTOMapper.getOutWithItems(itemRequestDto, itemViewWithRequest);

        when(userRepository.existsById(any())).thenReturn(true);
        when(requestRepository.findByOwner_IdNot(any(), any())).thenReturn(itemRequestDto);
        when(itemRepository.findByRequestIdIn(any())).thenReturn(itemViewWithRequest);

        assertThat(service.getAll(idOne, 0, 1)).hasSize(1);

        final InOrder inOrder = inOrder(userRepository, requestRepository, itemRepository);
        inOrder.verify(userRepository).existsById(any());
        inOrder.verify(requestRepository).findByOwner_IdNot(any(), any());
        inOrder.verify(itemRepository).findByRequestIdIn(any());

        assertThat(service.getAll(idOne, 0, 1)).isEqualTo(expectedDto);
    }

    @Test
    void getAll_whenUserNotExists_thenThrowUserNotFoundException() {
        when(userRepository.existsById(idOne)).thenReturn(false);

        assertThatThrownBy(() -> service.getAll(idOne, 0, 1))
                .isInstanceOf(UserNotFoundException.class);

        verify(requestRepository, never()).findByOwner_IdNot(any(), any());
        verify(itemRepository, never()).findByRequestIdIn(any());
    }


    @Test
    void getRequest() {
        final ItemRequest itemRequest = getItemRequest();
        final List<ItemViewWithRequest> itemViewWithRequest = List.of(getItemViewWithRequest());
        final ItemRequestOutWithItems expectedDto = ItemRequestDTOMapper.getOutWithItems(itemRequest, itemViewWithRequest);

        when(userRepository.existsById(any())).thenReturn(true);
        when(requestRepository.findById(any())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findByRequestId(any())).thenReturn(itemViewWithRequest);

        assertThat(service.getRequest(idOne, idOne)).isEqualTo(expectedDto);

        final InOrder inOrder = inOrder(userRepository, requestRepository, itemRepository);
        inOrder.verify(userRepository).existsById(any());
        inOrder.verify(requestRepository).findById(any());
        inOrder.verify(itemRepository).findByRequestId(any());
    }

    @Test
    void getRequest_whenUserNotExists_thenThrowUserNotFoundException() {
        when(userRepository.existsById(idOne)).thenReturn(false);

        assertThatThrownBy(() -> service.getRequest(idOne, idOne))
                .isInstanceOf(UserNotFoundException.class);

        verify(requestRepository, never()).findById(any());
        verify(itemRepository, never()).findByRequestId(any());
    }
}