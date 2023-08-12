package ru.practicum.shareit;

import org.junit.jupiter.api.extension.TestInstantiationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingOutDTO;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.comment.CommentView;
import ru.practicum.shareit.item.dto.item.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestOutWithItems;
import ru.practicum.shareit.request.dto.ItemRequestView;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserIdDTO;
import ru.practicum.shareit.user.dto.UserView;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractTest {
    protected final long idOne = 1;
    protected final String name = "SomeName";
    protected final String email = "email@mail.com";
    protected final String description = "SomeDescription";
    protected final String text = "SomeText";
    protected final boolean available = true;
    protected final Status status = Status.APPROVED;
    protected final LocalDateTime dateTime = LocalDateTime.of(2022, 3, 8, 10, 1, 2);
    protected final LocalDateTime endTime = dateTime.plusDays(2);
    protected final LocalDateTime nowTime = LocalDateTime.now();
    protected final Pageable pageable = PageRequest.of(0, 1);
    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    protected BookingOutDTO getBookingOutDTO() {
        return BookingOutDTO.builder()
                .id(idOne)
                .start(dateTime)
                .end(endTime)
                .status(status)
                .booker(new UserIdDTO(idOne))
                .item(new ItemIdAndName(idOne, name))
                .build();
    }

    protected Booking getBookingNoId() {
        return Booking.builder()
                .start(dateTime)
                .end(endTime)
                .booker(getUser())
                .item(getItem())
                .status(status)
                .build();
    }

    protected Booking getBooking() {
        final Booking booking = getBookingNoId();
        booking.setId(idOne);
        return booking;
    }

    protected BookingDTO getBookingDTO() {
        return BookingDTO.builder()
                .bookerId(idOne)
                .itemId(idOne)
                .start(dateTime)
                .end(endTime)
                .build();
    }

    protected String getSubstringText(String value) {
        if (!value.isBlank() && (value.length() > 3)) {
            return value.substring(1, value.length() - 1);
        }
        throw new TestInstantiationException("Bad string value: " + value);
    }

    protected BookingView getBookingView() {
        final BookingView projection = factory.createProjection(BookingView.class);
        projection.setId(idOne);
        projection.setStart(dateTime);
        projection.setEnd(endTime);
        projection.setStatus(status);
        projection.setBooker(getUserView());
        projection.setItem(getItemView());
        return projection;
    }

    protected CommentDTO getCommentDto() {
        return CommentDTO.builder()
                .id(idOne)
                .text(text)
                .authorName(name)
                .created(dateTime)
                .build();
    }

    protected Comment getCommentNoId() {
        return Comment.builder()
                .author(getUser())
                .item(getItem())
                .text(text)
                .created(dateTime)
                .build();
    }

    protected Comment getComment() {
        final Comment comment = getCommentNoId();
        comment.setId(idOne);
        return comment;
    }

    protected ItemOut getItemOut() {
        return ItemOut.outBuilder()
                .ownerId(idOne)
                .name(name)
                .description(description)
                .available(available)
                .requestId(idOne)
                .comments(List.of(getCommentDto()))
                .build();
    }

    protected ItemDTO getItemDtoNoId() {
        return ItemDTO.builder()
                .ownerId(idOne)
                .name(name)
                .description(description)
                .available(available)
                .requestId(idOne)
                .build();
    }

    protected ItemDTO getItemDto() {
        final ItemDTO itemDTO = getItemDtoNoId();
        itemDTO.setId(idOne);
        return itemDTO;
    }

    protected Item getItemNoId() {
        return Item.builder()
                .owner(getUser())
                .name(name)
                .description(description)
                .available(available)
                .request(getItemRequest())
                .build();
    }

    protected Item getItem() {
        final Item item = getItemNoId();
        item.setId(idOne);
        return item;
    }

    protected ItemRequestView getItemRequestView() {
        final ItemRequestView projection = factory.createProjection(ItemRequestView.class);
        projection.setId(idOne);
        return projection;
    }

    protected ItemViewWithRequest getItemViewWithRequest() {
        ItemViewWithRequest projection = factory.createProjection(ItemViewWithRequest.class);
        projection.setId(idOne);
        projection.setName(name);
        projection.setDescription(description);
        projection.setAvailable(available);
        projection.setRequest(getItemRequestView());
        return projection;
    }

    protected UserView getUserView() {
        UserView userView = factory.createProjection(UserView.class);
        userView.setId(idOne);
        userView.setName(name);
        return userView;
    }

    protected UserView getUserView(User user) {
        UserView userView = factory.createProjection(UserView.class);
        userView.setId(user.getId());
        userView.setName(user.getName());
        return userView;
    }

    protected ItemView getItemView() {
        ItemView itemView = factory.createProjection(ItemView.class);
        itemView.setId(idOne);
        itemView.setName(name);
        return itemView;
    }

    protected ItemView getItemView(Item item) {
        ItemView itemView = factory.createProjection(ItemView.class);
        itemView.setId(item.getId());
        itemView.setName(item.getName());
        return itemView;
    }

    protected CommentView getCommentView(Comment comment) {
        CommentView projection = factory.createProjection(CommentView.class);
        UserView author = getUserView();
        author.setId(comment.getAuthor().getId());
        author.setName(comment.getAuthorName());
        ItemView item = getItemView();
        item.setId(comment.getItem().getId());
        item.setName(comment.getItem().getName());
        projection.setId(comment.getId());
        projection.setText(comment.getText());
        projection.setCreated(comment.getCreated());
        projection.setAuthor(author);
        projection.setItem(item);
        return projection;
    }

    protected ItemRequestOutWithItems getItemRequestOutWithItems() {
        final ItemRequestDTO requestDTO = getItemRequestDto();
        final ItemInRequest inRequest = ItemInRequest.builder()
                .id(idOne)
                .name(name)
                .description(description)
                .available(available)
                .requestId(idOne)
                .build();
        final List<ItemInRequest> list = List.of(inRequest);
        return new ItemRequestOutWithItems(
                requestDTO.getId(),
                requestDTO.getDescription(),
                requestDTO.getCreated(),
                list
        );
    }

    protected ItemRequestDTO getItemRequestDto() {
        return ItemRequestDTO.builder()
                .id(idOne)
                .description(description)
                .created(dateTime)
                .build();
    }

    protected ItemRequest getItemRequestNoId() {
        return ItemRequest.builder()
                .description(description)
                .created(dateTime)
                .owner(getUser())
                .build();
    }

    protected ItemRequest getItemRequest() {
        final ItemRequest itemRequest = getItemRequestNoId();
        itemRequest.setId(idOne);
        return itemRequest;
    }

    protected UserDTO getUserDtoNoId() {
        return UserDTO.builder()
                .name(name)
                .email(email)
                .build();
    }

    protected UserDTO getUserDto() {
        final UserDTO userDTO = getUserDtoNoId();
        userDTO.setId(idOne);
        return userDTO;
    }

    protected User getUserNoId() {
        return User.builder()
                .name(name)
                .email(email)
                .build();
    }

    protected User getUser() {
        final User user = getUserNoId();
        user.setId(idOne);
        return user;
    }
}