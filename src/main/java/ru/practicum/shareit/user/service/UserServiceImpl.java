package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.user.UserEmailAlreadyExistException;
import ru.practicum.shareit.exception.user.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserDTOMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;

import static ru.practicum.shareit.user.UserLogMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private void saveWithEmailCheck(User user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserEmailAlreadyExistException(user.getEmail());
        }
    }

    @Override
    @Transactional
    public UserDTO addUser(UserDTO userDTO) {
        final User user = UserDTOMapper.toUserWhenCreate(userDTO);
        this.saveWithEmailCheck(user);
        log.info(USER_ADDED, user.getId(), user.getEmail());
        return UserDTOMapper.fromUser(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(long id, UserDTO userDTO) {
        final User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        final User updatedUser = UserDTOMapper.toUserWhenUpdate(user, userDTO);
        this.saveWithEmailCheck(updatedUser);
        log.info(USER_UPDATED, id);
        return UserDTOMapper.fromUser(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        log.info(USER_DELETED, id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getUser(long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        log.info(GET_USER, id);
        return UserDTOMapper.fromUser(user);
    }

    @Override
    public Collection<UserDTO> getAllUsers() {
        log.info(GET_USER_LIST);
        return userRepository.findAllBy();
    }
}