package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.exception.user.UserEmailAlreadyExistException;
import ru.practicum.shareit.exception.user.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserDTOMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest extends AbstractTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void addUser_whenUserDtoIsValid_thenAddUser() {
        final User user = getUser();
        final UserDTO expectedDTO = UserDTOMapper.fromUser(user);

        when(userRepository.save(any())).thenReturn(user);

        assertThat(userService.addUser(expectedDTO)).isEqualTo(expectedDTO);
    }

    @Test
    void addUser_whenUserEmailAlreadyExists_thenThrowUserEmailAlreadyExistException() {
        when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> userService.addUser(getUserDto()))
                .isInstanceOf(UserEmailAlreadyExistException.class);
    }


    @Test
    void updateUser_whenUserIsValid_thenUpdateUser() {
        final User user = getUser();
        final User updatedUser = getUser();
        updatedUser.setName("UpdatedName");
        updatedUser.setEmail("UpdatedEmail");
        final UserDTO expectedDTO = UserDTOMapper.fromUser(updatedUser);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);

        assertThat(userService.updateUser(user.getId(), expectedDTO)).isEqualTo(expectedDTO);

        final InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository).findById(user.getId());
        inOrder.verify(userRepository).save(any());
    }

    @Test
    void updateUser_whenUserNotFound_thenThrowUserNotFoundException() {
        final UserDTO userDTO = getUserDto();

        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(userDTO.getId(), userDTO))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_whenUserEmailAlreadyExists_thenThrowUserEmailAlreadyExistException() {
        final UserDTO userDTO = getUserDto();

        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(new User()));
        when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> userService.updateUser(userDTO.getId(), userDTO))
                .isInstanceOf(UserEmailAlreadyExistException.class);
    }

    @Test
    void deleteUser() {
        userService.deleteUser(anyLong());

        verify(userRepository).deleteById(anyLong());
    }

    @Test
    void getUser_whenUserIsValid_thenGetUser() {
        final User user = getUser();
        final UserDTO expectedDTO = UserDTOMapper.fromUser(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThat(userService.getUser(user.getId())).isEqualTo(expectedDTO);
    }

    @Test
    void getUser_whenUserIsNotExists_thenThrowUserNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(anyLong())).isInstanceOf(UserNotFoundException.class);
    }


    @Test
    void getAllUsers() {
        when(userRepository.findAllBy()).thenReturn(List.of(getUserDto()));

        assertThat(userService.getAllUsers()).hasSize(1);
        assertThat(userService.getAllUsers().get(0)).isEqualTo(getUserDto());
    }
}