package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserDTOMapperTest extends AbstractTest {

    @Test
    void toUserWhenCreate_ReturnUserWithoutId() {
        final UserDTO userDTO = getUserDto();
        final User expectedUser = getUser();
        final User actualUser = UserDTOMapper.toUserWhenCreate(userDTO);

        assertThat(actualUser.getId()).isEqualTo(null);
        assertThat(actualUser.getName()).isEqualTo(expectedUser.getName());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
    }

    @Test
    void toUserWhenUpdate_whenUpdateWithoutNameAndEmail_thenNameAndEmailIsNotUpdated() {
        final UserDTO userDTO = getUserDto();
        final User expectedUser = getUser();
        final User actualUser = UserDTOMapper.toUserWhenUpdate(getUser(), userDTO);

        assertThat(actualUser.getId()).isEqualTo(expectedUser.getId());
        assertThat(actualUser.getName()).isEqualTo(expectedUser.getName());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
    }

    @Test
    void toUserWhenUpdate_whenUpdateNameAndEmail_thenNameAndEmailIsUpdated() {
        final String newName = "UpdatedName";
        final String newEmail = "UpdatedEmail";
        final UserDTO userDTO = getUserDto();
        userDTO.setName(newName);
        userDTO.setEmail(newEmail);
        final User expectedUser = getUser();
        expectedUser.setName(newName);
        expectedUser.setEmail(newEmail);
        final User actualUser = UserDTOMapper.toUserWhenUpdate(getUser(), userDTO);

        assertThat(actualUser.getId()).isEqualTo(expectedUser.getId());
        assertThat(actualUser.getName()).isEqualTo(expectedUser.getName());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
    }

    @Test
    void toUserWhenUpdate_whenUpdateEmail_thenEmailIsUpdated() {
        final String newEmail = "UpdatedEmail";
        final UserDTO userDTO = getUserDto();
        userDTO.setEmail(newEmail);
        final User expectedUser = getUser();
        expectedUser.setEmail(newEmail);
        final User actualUser = UserDTOMapper.toUserWhenUpdate(getUser(), userDTO);

        assertThat(actualUser.getId()).isEqualTo(expectedUser.getId());
        assertThat(actualUser.getName()).isEqualTo(expectedUser.getName());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
    }

    @Test
    void toUserWhenUpdate_whenUpdateName_thenNameIsUpdated() {
        final String newName = "UpdatedName";
        final UserDTO userDTO = getUserDto();
        userDTO.setName(newName);
        final User expectedUser = getUser();
        expectedUser.setName(newName);
        final User actualUser = UserDTOMapper.toUserWhenUpdate(getUser(), userDTO);

        assertThat(actualUser.getId()).isEqualTo(expectedUser.getId());
        assertThat(actualUser.getName()).isEqualTo(expectedUser.getName());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
    }

    @Test
    void fromUser_ReturnUserDTO() {
        final User user = getUser();
        final UserDTO expectedUserDTO = getUserDto();
        final UserDTO actualUser = UserDTOMapper.fromUser(user);

        assertThat(actualUser.getId()).isEqualTo(expectedUserDTO.getId());
        assertThat(actualUser.getName()).isEqualTo(expectedUserDTO.getName());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUserDTO.getEmail());
    }
}