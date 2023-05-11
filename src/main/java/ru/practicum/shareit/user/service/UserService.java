package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    User addNewUser(User user);

    User updateUser(long id, UserDto userDto);

    void deleteUser(long id);

    User getUser(long id);

    Collection<User> getAllUsers();
}