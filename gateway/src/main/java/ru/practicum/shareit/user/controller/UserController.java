package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.utils.CreateInfo;
import ru.practicum.shareit.utils.UpdateInfo;

import static ru.practicum.shareit.user.utils.UserLogMessage.*;
import static ru.practicum.shareit.utils.PatternsApp.USERS_PREFIX;

@Controller
@RequestMapping(path = USERS_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@Validated(CreateInfo.class) @RequestBody UserDTO userDTO) {
        log.info(REQUEST_ADD_USER, userDTO.getEmail());
        return userClient.addUser(userDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id,
                       @Validated(UpdateInfo.class) @RequestBody UserDTO userDTO) {
        log.info(REQUEST_UPDATE_USER, id);
        return userClient.updateUser(id, userDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        log.info(REQUEST_GET_USER, id);
        return userClient.getUser(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        log.info(REQUEST_DELETE_USER, id);
        return userClient.deleteUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info(REQUEST_GET_USER_LIST);
        return userClient.getAllUsers();
    }
}