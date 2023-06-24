package ru.practicum.shareit.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class UserDtoMapper {
    public User toUserWhenCreate(UserDto userDto) {
        User user = new User();
        if (userDto.getId() != null) {
            user.setId(userDto.getId());
        }
        if ((userDto.getName() != null) && (!userDto.getName().isBlank())) {
            user.setName(userDto.getName());
        }
        if ((userDto.getEmail() != null) && (!userDto.getEmail().isBlank())) {
            user.setEmail(userDto.getEmail());
        }
        return user;
    }

    public User toUserWhenUpdate(User user, UserDto userDto) {
        final User updatedUser =
                User.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build();
        if ((userDto.getName() != null) && (!userDto.getName().isBlank())) {
            updatedUser.setName(userDto.getName());
        }
        if ((userDto.getEmail() != null) && (!userDto.getEmail().isBlank())) {
            updatedUser.setEmail(userDto.getEmail());
        }
        return updatedUser;
    }

    public UserDto fromUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}