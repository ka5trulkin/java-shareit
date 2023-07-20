package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.user.dto.UserView;

import java.time.LocalDateTime;

public interface CommentView {
    Long getId();

    String getText();

    LocalDateTime getCreated();

    UserView getAuthor();

    ItemView getItem();
}