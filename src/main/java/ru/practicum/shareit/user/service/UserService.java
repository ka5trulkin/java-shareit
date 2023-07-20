package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDTO;

import java.util.Collection;

public interface UserService {
    UserDTO addUser(UserDTO userDTO);

    UserDTO updateUser(long id, UserDTO userDTO);

    void deleteUser(long id);

    UserDTO getUser(long id);

    Collection<UserDTO> getAllUsers();
}