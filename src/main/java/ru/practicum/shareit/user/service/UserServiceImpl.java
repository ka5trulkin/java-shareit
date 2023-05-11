package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

import static ru.practicum.shareit.user.UserLogMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private User updateUserFromDto(User user, UserDto userDto) {
        final User updatedUser = User.builder().
                id(user.getId()).
                name(user.getName()).
                email(user.getEmail()).
                build();
        if ((userDto.getName() != null) && (!userDto.getName().isBlank())) {
            updatedUser.setName(userDto.getName());
        }
        if ((userDto.getEmail() != null) && (!userDto.getEmail().isBlank())) {
            updatedUser.setEmail(userDto.getEmail());
        }
        return updatedUser;
    }

    @Override
    public User addNewUser(User user) {
        userRepository.addNewUser(user);
        log.info(USER_ADDED.message(), user.getId(), user.getEmail());
        return user;
    }

    @Override
    public User updateUser(long id, UserDto userDto) {
        final User user = userRepository.getUser(id);
        final User updatedUser = this.updateUserFromDto(user, userDto);
        userRepository.updateUser(updatedUser);
        log.info(USER_UPDATED.message(), id);
        return updatedUser;
    }

    @Override
    public void deleteUser(long id) {
        log.info(USER_DELETED.message(), id);
        userRepository.deleteUser(id);
    }

    @Override
    public User getUser(long id) {
        User user = userRepository.getUser(id);
        log.info(GET_USER.message(), id);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info(GET_USER_LIST.message());
        return userRepository.getAllUsers();
    }
}