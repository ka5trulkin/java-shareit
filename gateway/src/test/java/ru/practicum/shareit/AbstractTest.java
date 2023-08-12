package ru.practicum.shareit;

import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;

public abstract class AbstractTest {
    protected final long idOne = 1;
    protected final String name = "SomeName";
    protected final String email = "email@mail.com";
    protected final String description = "SomeDescription";
    protected final String text = "SomeText";
    protected final boolean available = true;
    protected final LocalDateTime nowTime = LocalDateTime.now();
    protected final LocalDateTime endTime = nowTime.plusDays(2);

    protected BookingDTO getBookingDTO() {
        return BookingDTO.builder()
                .bookerId(idOne)
                .itemId(idOne)
                .start(nowTime)
                .end(endTime)
                .build();
    }

    protected CommentDTO getCommentDto() {
        return CommentDTO.builder()
                .id(idOne)
                .text(text)
                .authorName(name)
                .created(nowTime)
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
}