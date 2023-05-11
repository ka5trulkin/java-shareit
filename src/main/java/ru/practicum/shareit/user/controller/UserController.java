package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;

import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
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
    User addNewUser(@Valid @RequestBody User user) {
        log.info(REQUEST_ADD_USER.message(), user.getEmail());
        return userService.addNewUser(user);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    User updateUser(@PathVariable long id, @Valid @RequestBody UserDto userDto) {
        log.info(REQUEST_UPDATE_USER.message(), id);
        return userService.updateUser(id, userDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    User getUser(@PathVariable long id) {
        log.info(REQUEST_GET_USER.message(), id);
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    void deleteUser(@PathVariable long id) {
        log.info(REQUEST_DELETE_USER.message(), id);
        userService.deleteUser(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    Collection<User> getAllUsers() {
        log.info(REQUEST_GET_USER_LIST.message());
        return userService.getAllUsers();
    }
}