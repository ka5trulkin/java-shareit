package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.service.CreateInfo;
import ru.practicum.shareit.service.UpdateInfo;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
import static ru.practicum.shareit.user.UserLogMessage.*;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    UserDTO createUser(@Validated(CreateInfo.class) @RequestBody UserDTO userDTO) {
        log.info(REQUEST_ADD_USER, userDTO.getEmail());
        return userService.addUser(userDTO);
    }

    @PatchMapping("/{id}")
    UserDTO updateUser(@PathVariable Long id,
                       @Validated(UpdateInfo.class) @RequestBody UserDTO userDTO) {
        log.info(REQUEST_UPDATE_USER, id);
        return userService.updateUser(id, userDTO);
    }

    @GetMapping("/{id}")
    UserDTO getUser(@PathVariable Long id) {
        log.info(REQUEST_GET_USER, id);
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable Long id) {
        log.info(REQUEST_DELETE_USER, id);
        userService.deleteUser(id);
    }

    @GetMapping
    Collection<UserDTO> getAllUsers() {
        log.info(REQUEST_GET_USER_LIST);
        return userService.getAllUsers();
    }
}