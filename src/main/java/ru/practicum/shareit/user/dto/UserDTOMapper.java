package ru.practicum.shareit.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

@UtilityClass
public class UserDTOMapper {
    public User toUserWhenCreate(UserDTO userDTO) {
        User user = new User();
        if (userDTO.getId() != null) {
            user.setId(userDTO.getId());
        }
        if ((userDTO.getName() != null) && (!userDTO.getName().isBlank())) {
            user.setName(userDTO.getName());
        }
        if ((userDTO.getEmail() != null) && (!userDTO.getEmail().isBlank())) {
            user.setEmail(userDTO.getEmail());
        }
        return user;
    }

    public User toUserWhenUpdate(User user, UserDTO userDTO) {
        final User updatedUser =
                User.builder()
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

    public Collection<UserDTO> fromUserCollection(Collection<User> collection) {
        return collection.stream()
                .map(UserDTOMapper::fromUser)
                .collect(Collectors.toList());
    }
}