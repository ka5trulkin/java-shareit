package ru.practicum.shareit.user.dto;

public interface UserView {
    Long getId();

    String getName();

    void setId(Long id);

    void setName(String name);
}