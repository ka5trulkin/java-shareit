package ru.practicum.shareit.user.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDTO;

import java.util.Collection;

@Transactional(readOnly = true)
public interface UserService {
    @Transactional
    UserDTO addUser(UserDTO userDTO);

    @Transactional
    UserDTO updateUser(long id, UserDTO userDTO);

    @Transactional
    void deleteUser(long id);

    UserDTO getUser(long id);

    Collection<UserDTO> getAllUsers();
}