package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.service.CreateInfo;
import ru.practicum.shareit.service.UpdateInfo;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
import static ru.practicum.shareit.user.UserLogMessage.*;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    UserDto createUser(@Validated(CreateInfo.class) @RequestBody UserDto userDto) {
        log.info(REQUEST_ADD_USER, userDto.getEmail());
        return userService.addUser(userDto);
    }

    @PatchMapping("/{id}")
    UserDto updateUser(@PathVariable Long id,
                    @Validated(UpdateInfo.class) @RequestBody UserDto userDto) {
        log.info(REQUEST_UPDATE_USER, id);
        return userService.updateUser(id, userDto);
    }

    @GetMapping("/{id}")
    User getUser(@PathVariable Long id) {
        log.info(REQUEST_GET_USER, id);
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable Long id) {
        log.info(REQUEST_DELETE_USER, id);
        userService.deleteUser(id);
    }

    @GetMapping
    Collection<User> getAllUsers() {
        log.info(REQUEST_GET_USER_LIST);
        return userService.getAllUsers();
    }
}