package ru.practicum.shareit.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class UserDTOMapper {
    public User toUserWhenCreate(UserDTO userDTO) {
        return User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .build();
    }

    public User toUserWhenUpdate(User user, UserDTO userDTO) {
        final User updatedUser = User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        if ((userDTO.getName() != null) && (!userDTO.getName().isBlank())) {
            updatedUser.setName(userDTO.getName());
        }
        if ((userDTO.getEmail() != null) && (!userDTO.getEmail().isBlank())) {
            updatedUser.setEmail(userDTO.getEmail());
        }
        return updatedUser;
    }

    public UserDTO fromUser(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}