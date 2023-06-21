package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserDtoMapper {
    public static User toUserWhenUpdate(User user, UserDto userDto) {
        final User updatedUser = User.builder()
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
}