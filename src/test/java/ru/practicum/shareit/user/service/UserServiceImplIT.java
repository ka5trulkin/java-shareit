package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceImplIT extends AbstractTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    private User user;
    private Long userId;

    @BeforeEach
    void beforeEach() {
        user = getUserNoId();
        userId = userRepository.save(user).getId();
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }


    @Test
    void getUserTest() {
        final UserDTO actual = userService.getUser(userId);

        assertThat(actual.getId()).isEqualTo(userId);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getEmail()).isEqualTo(email);
    }
}