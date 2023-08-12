package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTOMapper;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.item.dto.item.ItemDTOMapper;
import ru.practicum.shareit.item.dto.comment.CommentView;
import ru.practicum.shareit.item.dto.item.ItemOut;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemDTOMapperTest extends AbstractTest {
    @Test
    void toItem() {
        final Item expectedResult = getItemNoId();
        final Item actualResult = ItemDTOMapper.toItem(idOne, getItemDtoNoId());

        assertThat(actualResult.getId()).isNull();
        assertThat(actualResult.getOwner()).isInstanceOf(User.class);
        assertThat(actualResult.getIdOwner()).isNotNull();
        assertThat(actualResult.getIdOwner()).isEqualTo(expectedResult.getIdOwner());
        assertThat(actualResult.getName()).isEqualTo(expectedResult.getName());
        assertThat(actualResult.getDescription()).isEqualTo(expectedResult.getDescription());
        assertThat(actualResult.isAvailable()).isEqualTo(expectedResult.isAvailable());
        assertThat(actualResult.getRequest()).isInstanceOf(ItemRequest.class);
        assertThat(actualResult.getRequestsId()).isNotNull();
        assertThat(actualResult.getRequestsId()).isEqualTo(expectedResult.getRequestsId());
    }

    @Test
    void toItemWhenUpdate_whenUpdateName() {
        String newName = "NewName";
        final Item expectedResult = getItemNoId();
        expectedResult.setName(newName);
        final ItemDTO itemDTO = getItemDtoNoId();
        itemDTO.setName(newName);
        final Item actualResult = ItemDTOMapper.toItemWhenUpdate(getItemNoId(), itemDTO);

        assertThat(actualResult.getId()).isNull();
        assertThat(actualResult.getOwner()).isInstanceOf(User.class);
        assertThat(actualResult.getIdOwner()).isNotNull();
        assertThat(actualResult.getIdOwner()).isEqualTo(expectedResult.getIdOwner());
        assertThat(actualResult.getName()).isEqualTo(expectedResult.getName());
        assertThat(actualResult.getDescription()).isEqualTo(expectedResult.getDescription());
        assertThat(actualResult.isAvailable()).isEqualTo(expectedResult.isAvailable());
        assertThat(actualResult.getRequest()).isInstanceOf(ItemRequest.class);
        assertThat(actualResult.getRequestsId()).isNotNull();
        assertThat(actualResult.getRequestsId()).isEqualTo(expectedResult.getRequestsId());
    }

    @Test
    void toItemWhenUpdate_whenUpdateDescription() {
        String newDescription = "NewDescription";
        final Item expectedResult = getItemNoId();
        expectedResult.setDescription(newDescription);
        final ItemDTO itemDTO = getItemDtoNoId();
        itemDTO.setDescription(newDescription);
        final Item actualResult = ItemDTOMapper.toItemWhenUpdate(getItemNoId(), itemDTO);

        assertThat(actualResult.getId()).isNull();
        assertThat(actualResult.getOwner()).isInstanceOf(User.class);
        assertThat(actualResult.getIdOwner()).isNotNull();
        assertThat(actualResult.getIdOwner()).isEqualTo(expectedResult.getIdOwner());
        assertThat(actualResult.getName()).isEqualTo(expectedResult.getName());
        assertThat(actualResult.getDescription()).isEqualTo(expectedResult.getDescription());
        assertThat(actualResult.isAvailable()).isEqualTo(expectedResult.isAvailable());
        assertThat(actualResult.getRequest()).isInstanceOf(ItemRequest.class);
        assertThat(actualResult.getRequestsId()).isNotNull();
        assertThat(actualResult.getRequestsId()).isEqualTo(expectedResult.getRequestsId());
    }

    @Test
    void toItemWhenUpdate_whenUpdateAvailable() {
        final Item expectedResult = getItemNoId();
        expectedResult.setAvailable(false);
        final ItemDTO itemDTO = getItemDtoNoId();
        itemDTO.setAvailable(false);
        final Item actualResult = ItemDTOMapper.toItemWhenUpdate(getItemNoId(), itemDTO);

        assertThat(actualResult.getId()).isNull();
        assertThat(actualResult.getOwner()).isInstanceOf(User.class);
        assertThat(actualResult.getIdOwner()).isNotNull();
        assertThat(actualResult.getIdOwner()).isEqualTo(expectedResult.getIdOwner());
        assertThat(actualResult.getName()).isEqualTo(expectedResult.getName());
        assertThat(actualResult.getDescription()).isEqualTo(expectedResult.getDescription());
        assertThat(actualResult.isAvailable()).isEqualTo(expectedResult.isAvailable());
        assertThat(actualResult.getRequest()).isInstanceOf(ItemRequest.class);
        assertThat(actualResult.getRequestsId()).isNotNull();
        assertThat(actualResult.getRequestsId()).isEqualTo(expectedResult.getRequestsId());
    }

    @Test
    void fromItem() {
        final ItemDTO expectedResult = getItemDto();
        final ItemDTO actualResult = ItemDTOMapper.fromItem(getItem());

        assertThat(actualResult.getId()).isEqualTo(expectedResult.getId());
        assertThat(actualResult.getOwnerId()).isEqualTo(expectedResult.getOwnerId());
        assertThat(actualResult.getName()).isEqualTo(expectedResult.getName());
        assertThat(actualResult.getDescription()).isEqualTo(expectedResult.getDescription());
        assertThat(actualResult.getAvailable()).isEqualTo(expectedResult.getAvailable());
        assertThat(actualResult.getRequestId()).isEqualTo(expectedResult.getRequestId());
    }

    @Test
    void fromBookingView() {
        final ItemDTO itemDto = getItemDto();
        final CommentDTO commentDTO = CommentDTOMapper.fromComment(getComment());
        final List<CommentDTO> comments = List.of(commentDTO);
        final ItemOut actualResult = ItemDTOMapper.fromBookingView(itemDto, comments);

        assertThat(actualResult.getId()).isEqualTo(itemDto.getId());
        assertThat(actualResult.getOwnerId()).isEqualTo(itemDto.getOwnerId());
        assertThat(actualResult.getName()).isEqualTo(itemDto.getName());
        assertThat(actualResult.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(actualResult.getAvailable()).isEqualTo(itemDto.getAvailable());
        assertThat(actualResult.getRequestId()).isEqualTo(itemDto.getRequestId());
        assertThat(actualResult.getLastBooking()).isNull();
        assertThat(actualResult.getNextBooking()).isNull();
        assertThat(actualResult.getComments()).isNotEmpty().hasSize(1);
        assertThat(actualResult.getComments().get(0).getId()).isEqualTo(commentDTO.getId());
        assertThat(actualResult.getComments().get(0).getText()).isEqualTo(commentDTO.getText());
        assertThat(actualResult.getComments().get(0).getAuthorName()).isEqualTo(commentDTO.getAuthorName());
        assertThat(actualResult.getComments().get(0).getCreated()).isEqualTo(commentDTO.getCreated());
    }

    @Test
    void testFromBookingView() {
        final ItemDTO itemDto = getItemDto();
        final BookingView lastBooking = getBookingView();
        final BookingView nextBooking = getBookingView();
        final CommentDTO commentDTO = CommentDTOMapper.fromComment(getComment());
        final List<CommentDTO> comments = List.of(commentDTO);
        final ItemOut actualResult = ItemDTOMapper.fromBookingView(itemDto, lastBooking, nextBooking, comments);

        assertThat(actualResult.getId()).isEqualTo(itemDto.getId());
        assertThat(actualResult.getOwnerId()).isEqualTo(itemDto.getOwnerId());
        assertThat(actualResult.getName()).isEqualTo(itemDto.getName());
        assertThat(actualResult.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(actualResult.getAvailable()).isEqualTo(itemDto.getAvailable());
        assertThat(actualResult.getRequestId()).isEqualTo(itemDto.getRequestId());
        assertThat(actualResult.getLastBooking().getId()).isEqualTo(lastBooking.getId());
        assertThat(actualResult.getLastBooking().getBookerId()).isEqualTo(lastBooking.getBooker().getId());
        assertThat(actualResult.getLastBooking().getStart()).isEqualTo(lastBooking.getStart());
        assertThat(actualResult.getLastBooking().getEnd()).isEqualTo(lastBooking.getEnd());
        assertThat(actualResult.getNextBooking().getId()).isEqualTo(nextBooking.getId());
        assertThat(actualResult.getNextBooking().getBookerId()).isEqualTo(nextBooking.getBooker().getId());
        assertThat(actualResult.getNextBooking().getStart()).isEqualTo(nextBooking.getStart());
        assertThat(actualResult.getNextBooking().getEnd()).isEqualTo(nextBooking.getEnd());
        assertThat(actualResult.getComments()).isNotEmpty().hasSize(1);
        assertThat(actualResult.getComments().get(0).getId()).isEqualTo(commentDTO.getId());
        assertThat(actualResult.getComments().get(0).getText()).isEqualTo(commentDTO.getText());
        assertThat(actualResult.getComments().get(0).getAuthorName()).isEqualTo(commentDTO.getAuthorName());
        assertThat(actualResult.getComments().get(0).getCreated()).isEqualTo(commentDTO.getCreated());
    }

    @Test
    void fromBookingViewCollection() {
        final ItemDTO itemDto = getItemDto();
        final List<ItemDTO> itemDTOs = List.of(itemDto);
        final BookingView lastBooking = getBookingView();
        final BookingView nextBooking = getBookingView();
        nextBooking.setStart(LocalDateTime.now().plusDays(1));
        nextBooking.setEnd(LocalDateTime.now().plusDays(2));
        final List<BookingView> bookingViews = List.of(lastBooking, nextBooking);
        final Comment comment = getComment();
        final List<CommentView> commentViews = List.of(getCommentView(comment));
        final List<ItemOut> actualResult = ItemDTOMapper.fromBookingViewCollection(itemDTOs, bookingViews, commentViews);

        assertThat(actualResult).isNotEmpty().hasSize(1);
        assertThat(actualResult.get(0).getId()).isEqualTo(itemDto.getId());
        assertThat(actualResult.get(0).getOwnerId()).isEqualTo(itemDto.getOwnerId());
        assertThat(actualResult.get(0).getName()).isEqualTo(itemDto.getName());
        assertThat(actualResult.get(0).getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(actualResult.get(0).getAvailable()).isEqualTo(itemDto.getAvailable());
        assertThat(actualResult.get(0).getRequestId()).isEqualTo(itemDto.getRequestId());
        assertThat(actualResult.get(0).getLastBooking().getId()).isEqualTo(lastBooking.getId());
        assertThat(actualResult.get(0).getLastBooking().getBookerId()).isEqualTo(lastBooking.getBooker().getId());
        assertThat(actualResult.get(0).getLastBooking().getStart()).isEqualTo(lastBooking.getStart());
        assertThat(actualResult.get(0).getLastBooking().getEnd()).isEqualTo(lastBooking.getEnd());
        assertThat(actualResult.get(0).getNextBooking().getId()).isEqualTo(nextBooking.getId());
        assertThat(actualResult.get(0).getNextBooking().getBookerId()).isEqualTo(nextBooking.getBooker().getId());
        assertThat(actualResult.get(0).getNextBooking().getStart()).isEqualTo(nextBooking.getStart());
        assertThat(actualResult.get(0).getNextBooking().getEnd()).isEqualTo(nextBooking.getEnd());
        assertThat(actualResult.get(0).getComments()).isNotEmpty().hasSize(1);
        assertThat(actualResult.get(0).getComments().get(0).getId()).isEqualTo(comment.getId());
        assertThat(actualResult.get(0).getComments().get(0).getText()).isEqualTo(comment.getText());
        assertThat(actualResult.get(0).getComments().get(0).getAuthorName()).isEqualTo(comment.getAuthorName());
        assertThat(actualResult.get(0).getComments().get(0).getCreated()).isEqualTo(comment.getCreated());
    }
}