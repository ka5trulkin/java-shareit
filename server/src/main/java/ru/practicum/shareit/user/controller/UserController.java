package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static ru.practicum.shareit.user.UserLogMessage.*;
import static ru.practicum.shareit.utils.PatternsApp.USERS_PREFIX;

@Slf4j
@RestController
@RequestMapping(path = USERS_PREFIX)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        log.info(REQUEST_ADD_USER, userDTO.getEmail());
        return userService.addUser(userDTO);
    }

    @PatchMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id,
                              @RequestBody UserDTO userDTO) {
        log.info(REQUEST_UPDATE_USER, id);
        return userService.updateUser(id, userDTO);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        log.info(REQUEST_GET_USER, id);
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info(REQUEST_DELETE_USER, id);
        userService.deleteUser(id);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        log.info(REQUEST_GET_USER_LIST);
        return userService.getAllUsers();
    }
}