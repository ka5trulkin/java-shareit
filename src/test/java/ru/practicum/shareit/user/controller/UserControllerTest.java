package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest extends AbstractTest {
    @Mock
    UserService userService;
    @InjectMocks
    UserController controller;

    @Test
    void createUser() {
        final UserDTO expected = getUserDto();

        when(userService.addUser(any())).thenReturn(expected);

        assertThat(controller.createUser(getUserDtoNoId())).isEqualTo(expected);
    }

    @Test
    void updateUser() {
        final UserDTO expected = getUserDto();

        when(userService.updateUser(anyLong(), any())).thenReturn(expected);

        assertThat(controller.updateUser(idOne, getUserDto())).isEqualTo(expected);
    }

    @Test
    void getUserTest() {
        final UserDTO expected = getUserDto();

        when(userService.getUser(anyLong())).thenReturn(expected);

        assertThat(controller.getUser(idOne)).isEqualTo(expected);
    }

    @Test
    void deleteUser() {
        doNothing().when(userService).deleteUser(anyLong());

        controller.deleteUser(idOne);

        verify(userService).deleteUser(idOne);
    }

    @Test
    void getAllUsers() {
        final List<UserDTO> expected = List.of(getUserDto());

        when(userService.getAllUsers()).thenReturn(expected);

        final List<UserDTO> actual = controller.getAllUsers();

        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual).isEqualTo(expected);
    }
}