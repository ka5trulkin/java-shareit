package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserDTOMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDAO;

import java.util.Collection;

import static ru.practicum.shareit.user.UserLogMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceInMemory implements UserService {
    private final UserDAO userRepository;

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        final User user = UserDTOMapper.toUserWhenCreate(userDTO);
        userRepository.addUser(user);
        log.info(USER_ADDED, user.getId(), user.getEmail());
        return UserDTOMapper.fromUser(user);
    }

    @Override
    public UserDTO updateUser(long id, UserDTO userDTO) {
        final User user = userRepository.getUser(id);
        final User updatedUser = UserDTOMapper.toUserWhenUpdate(user, userDTO);
        userRepository.updateUser(updatedUser);
        log.info(USER_UPDATED, id);
        return UserDTOMapper.fromUser(updatedUser);
    }

    @Override
    public void deleteUser(long id) {
        log.info(USER_DELETED, id);
        userRepository.deleteUser(id);
    }

    @Override
    public UserDTO getUser(long id) {
        final User user = userRepository.getUser(id);
        log.info(GET_USER, id);
        return UserDTOMapper.fromUser(user);
    }

    @Override
    public Collection<UserDTO> getAllUsers() {
        log.info(GET_USER_LIST);
        return UserDTOMapper.fromUserCollection(userRepository.getAllUsers());
    }
}