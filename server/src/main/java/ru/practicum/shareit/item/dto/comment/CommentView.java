package ru.practicum.shareit.item.dto.comment;

import ru.practicum.shareit.item.dto.item.ItemView;
import ru.practicum.shareit.user.dto.UserView;

import java.time.LocalDateTime;

public interface CommentView {
    Long getId();

    String getText();

    LocalDateTime getCreated();

    UserView getAuthor();

    ItemView getItem();

    void setId(Long id);

    void setText(String text);

    void setCreated(LocalDateTime created);

    void setAuthor(UserView author);

    void setItem(ItemView item);
}