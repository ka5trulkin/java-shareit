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

import java.util.List;

import static ru.practicum.shareit.user.UserLogMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private User saveWithEmailCheck(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserEmailAlreadyExistException(user.getEmail());
        }
    }

    @Override
    @Transactional
    public UserDTO addUser(UserDTO userDTO) {
        User user = UserDTOMapper.toUserWhenCreate(userDTO);
        user = this.saveWithEmailCheck(user);
        log.info(USER_ADDED, user.getId(), user.getEmail());
        return UserDTOMapper.fromUser(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(long id, UserDTO userDTO) {
        final User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        User updatedUser = UserDTOMapper.toUserWhenUpdate(user, userDTO);
        updatedUser = this.saveWithEmailCheck(updatedUser);
        log.info(USER_UPDATED, id);
        return UserDTOMapper.fromUser(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
        log.info(USER_DELETED, id);
    }

    @Override
    public UserDTO getUser(long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        log.info(GET_USER, id);
        return UserDTOMapper.fromUser(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info(GET_USER_LIST);
        return userRepository.findAllBy();
    }
}