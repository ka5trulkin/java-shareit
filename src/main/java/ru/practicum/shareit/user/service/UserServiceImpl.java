package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

import static ru.practicum.shareit.user.UserLogMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        final User user = UserDtoMapper.toUserWhenCreate(userDto);
        userRepository.addUser(user);
        log.info(USER_ADDED, user.getId(), user.getEmail());
        return UserDtoMapper.fromUser(user);
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        User user = userRepository.getUser(id);
        final User updatedUser = UserDtoMapper.toUserWhenUpdate(user, userDto);
        userRepository.updateUser(updatedUser);
        log.info(USER_UPDATED, id);
        return UserDtoMapper.fromUser(updatedUser);
    }

    @Override
    public void deleteUser(long id) {
        log.info(USER_DELETED, id);
        userRepository.deleteUser(id);
    }

    @Override
    public User getUser(long id) {
        User user = userRepository.getUser(id);
        log.info(GET_USER, id);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info(GET_USER_LIST);
        return userRepository.getAllUsers();
    }
}