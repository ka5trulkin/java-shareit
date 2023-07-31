package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.base.RequestException;
import ru.practicum.shareit.exception.item.ItemNotFoundException;
import ru.practicum.shareit.exception.item.OwnerNotFoundException;
import ru.practicum.shareit.exception.item_request.ItemRequestNotFoundException;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTOMapper;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.item.dto.item.ItemDTOMapper;
import ru.practicum.shareit.item.dto.comment.CommentView;
import ru.practicum.shareit.item.dto.item.ItemOut;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest extends AbstractTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void addItem() {
        final ItemDTO expectedResult = getItemDto();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.save(any())).thenReturn(getItem());

        assertThat(itemService.addItem(idOne, getItemDtoNoId())).isEqualTo(expectedResult);

        final InOrder inOrder = inOrder(userRepository, itemRequestRepository, itemRepository);
        inOrder.verify(userRepository).existsById(anyLong());
        inOrder.verify(itemRequestRepository).existsById(anyLong());
        inOrder.verify(itemRepository).save(any());
    }

    @Test
    void addItem_whenUserNotExists_thenThrowOwnerNotFoundException() {
        when(userRepository.existsById(anyLong())).thenThrow(OwnerNotFoundException.class);

        assertThatThrownBy(() -> itemService.addItem(idOne, getItemDtoNoId()))
                .isInstanceOf(OwnerNotFoundException.class);

        verify(itemRepository, never()).save(any());
    }

    @Test
    void addItem_whenRequestNotExists_thenThrowItemRequestNotFoundException() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.existsById(anyLong())).thenThrow(ItemRequestNotFoundException.class);

        assertThatThrownBy(() -> itemService.addItem(idOne, getItemDtoNoId()))
                .isInstanceOf(ItemRequestNotFoundException.class);

        verify(itemRepository, never()).save(any());
    }

    @Test
    void addComment() {
        final CommentDTO expectedResult = CommentDTOMapper.fromComment(getComment());

        when(bookingRepository.getByBookerIdAndItemId(anyLong(), anyLong(), any()))
                .thenReturn(Optional.of(getBookingView()));
        when(commentRepository.save(any())).thenReturn(getComment());

        assertThat(itemService.addComment(idOne, idOne, new CommentDTO())).isEqualTo(expectedResult);

        final InOrder inOrder = inOrder(bookingRepository, commentRepository);
        inOrder.verify(bookingRepository).getByBookerIdAndItemId(anyLong(), anyLong(), any());
        inOrder.verify(commentRepository).save(any());
    }

    @Test
    void addComment_whenBookingNotExists_thenThrowRequestException() {
        when(bookingRepository.getByBookerIdAndItemId(anyLong(), anyLong(), any())).thenThrow(RequestException.class);

        assertThatThrownBy(() -> itemService.addComment(idOne, idOne, new CommentDTO()))
                .isInstanceOf(RequestException.class);

        verify(commentRepository, never()).save(any());
    }

    @Test
    void updateItem() {
        final ItemDTO expectedResult = getItemDto();

        when(itemRepository.findByIdAndOwnerId(anyLong(), anyLong())).thenReturn(Optional.of(getItem()));
        when(itemRepository.save(any())).thenReturn(getItem());

        assertThat(itemService.updateItem(idOne, idOne, getItemDto())).isEqualTo(expectedResult);

        final InOrder inOrder = inOrder(itemRepository);
        inOrder.verify(itemRepository).findByIdAndOwnerId(anyLong(), anyLong());
        inOrder.verify(itemRepository).save(any());
    }

    @Test
    void updateItem_whenItemNotExists_thenThrowItemNotFoundException() {
        when(itemRepository.findByIdAndOwnerId(anyLong(), anyLong())).thenThrow(ItemNotFoundException.class);

        assertThatThrownBy(() -> itemService.updateItem(idOne, idOne, getItemDto()))
                .isInstanceOf(ItemNotFoundException.class);

        verify(itemRepository, never()).save(any());
    }

    @Test
    void getItemById_whenUserIsOwner() {
        final ItemDTO expectedResult = getItemDto();
        final List<CommentView> commentViews = List.of(getCommentView(getComment()));

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(getItem()));
        when(commentRepository.findByItem_Id(anyLong())).thenReturn(commentViews);
        when(bookingRepository.getLastBooking(anyLong(), anyLong(), any())).thenReturn(Optional.of(getBookingView()));
        when(bookingRepository.getNextBooking(anyLong(), anyLong(), any())).thenReturn(Optional.of(getBookingView()));

        final ItemOut actualResult = itemService.getItemById(idOne, idOne);

        final InOrder inOrder = inOrder(itemRepository, commentRepository, bookingRepository);
        inOrder.verify(itemRepository).findById(anyLong());
        inOrder.verify(commentRepository).findByItem_Id(anyLong());
        inOrder.verify(bookingRepository).getLastBooking(anyLong(), anyLong(), any());
        inOrder.verify(bookingRepository).getNextBooking(anyLong(), anyLong(), any());

        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.getNextBooking()).isNotNull();
        assertThat(actualResult.getLastBooking()).isNotNull();
        assertThat(actualResult.getComments()).isNotEmpty().hasSize(1);
    }

    @Test
    void getItemById_whenUserIsNotOwner() {
        final ItemDTO expectedResult = getItemDto();
        final List<CommentView> commentViews = List.of(getCommentView(getComment()));
        final long notOwnerId = idOne + 1;

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(getItem()));
        when(commentRepository.findByItem_Id(anyLong())).thenReturn(commentViews);

        final ItemOut actualResult = itemService.getItemById(idOne, notOwnerId);

        final InOrder inOrder = inOrder(itemRepository, commentRepository);
        inOrder.verify(itemRepository).findById(anyLong());
        inOrder.verify(commentRepository).findByItem_Id(anyLong());

        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.getNextBooking()).isNull();
        assertThat(actualResult.getLastBooking()).isNull();
        assertThat(actualResult.getComments()).isNotEmpty().hasSize(1);
    }

    @Test
    void getItemById_whenItemNotFound_thenThrowItemNotFoundException() {
        when(itemRepository.findById(anyLong())).thenThrow(ItemNotFoundException.class);

        assertThatThrownBy(() -> itemService.getItemById(idOne, idOne))
                .isInstanceOf(ItemNotFoundException.class);

        verify(commentRepository, never()).findByItem_Id(anyLong());
        verify(bookingRepository, never()).getNextBooking(anyLong(), anyLong(), any());
        verify(bookingRepository, never()).getLastBooking(anyLong(), anyLong(), any());
    }

    @Test
    void getItemsByOwner() {
        final List<ItemDTO> itemDTOs = List.of(getItemDto());
        final List<BookingView> bookingViews = List.of(getBookingView());
        final List<CommentView> commentViews = List.of(getCommentView(getComment()));
        final List<ItemOut> expectedResult =
                ItemDTOMapper.fromBookingViewCollection(itemDTOs, bookingViews, commentViews);

        when(itemRepository.findByOwnerId(anyLong(), any())).thenReturn(itemDTOs);
        when(bookingRepository.findByItemIdIn(any())).thenReturn(bookingViews);
        when(commentRepository.findByItem_IdIn(any())).thenReturn(commentViews);

        final List<ItemOut> actualResult = itemService.getItemsByOwner(idOne, 0, 1);

        assertThat(actualResult).isNotEmpty().hasSize(1);
        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.get(0).getNextBooking()).isEqualTo(expectedResult.get(0).getNextBooking());
        assertThat(actualResult.get(0).getLastBooking()).isEqualTo(expectedResult.get(0).getLastBooking());
        assertThat(actualResult.get(0).getComments()).isEqualTo(expectedResult.get(0).getComments());

        final InOrder inOrder = inOrder(itemRepository, bookingRepository, commentRepository);
        inOrder.verify(itemRepository).findByOwnerId(anyLong(), any());
        inOrder.verify(bookingRepository).findByItemIdIn(any());
        inOrder.verify(commentRepository).findByItem_IdIn(any());
    }

    @Test
    void getItemsBySearch() {
        final List<ItemDTO> itemDTOs = List.of(getItemDto());
        final List<BookingView> bookingViews = List.of(getBookingView());
        final List<CommentView> commentViews = List.of(getCommentView(getComment()));
        final List<ItemOut> expectedResult =
                ItemDTOMapper.fromBookingViewCollection(itemDTOs, bookingViews, commentViews);

        when(itemRepository.findByNameOrDescription(any(), any())).thenReturn(itemDTOs);
        when(bookingRepository.findByItemIdIn(any())).thenReturn(bookingViews);
        when(commentRepository.findByItem_IdIn(any())).thenReturn(commentViews);

        final List<ItemOut> actualResult = itemService.getItemsBySearch(text, 0, 1);

        assertThat(actualResult).isNotEmpty().hasSize(1);
        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.get(0).getNextBooking()).isEqualTo(expectedResult.get(0).getNextBooking());
        assertThat(actualResult.get(0).getLastBooking()).isEqualTo(expectedResult.get(0).getLastBooking());
        assertThat(actualResult.get(0).getComments()).isEqualTo(expectedResult.get(0).getComments());

        final InOrder inOrder = inOrder(itemRepository, bookingRepository, commentRepository);
        inOrder.verify(itemRepository).findByNameOrDescription(any(), any());
        inOrder.verify(bookingRepository).findByItemIdIn(any());
        inOrder.verify(commentRepository).findByItem_IdIn(any());
    }

    @Test
    void getItemsBySearch_whenTextIsBlank_thenReturnEmptyList() {
        assertThat(itemService.getItemsBySearch(" ", 0, 1)).isEmpty();

        final InOrder inOrder = inOrder(itemRepository);
        inOrder.verify(itemRepository, never()).findByNameOrDescription(any(), any());
    }

    @Test
    void getItemsBySearch_whenTextIsNull_thenReturnEmptyList() {
        assertThat(itemService.getItemsBySearch(null, 0, 1)).isEmpty();

        final InOrder inOrder = inOrder(itemRepository);
        inOrder.verify(itemRepository, never()).findByNameOrDescription(any(), any());
    }
}