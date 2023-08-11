package ru.practicum.shareit.user.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryIT extends AbstractTest {
    @Autowired
    private UserRepository userRepository;
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
    void findAllBy() {
        final UserDTO userDto = getUserDtoNoId();
        userDto.setId(userId);
        final List<UserDTO> actualResult = List.of(userDto);

        assertThat(userRepository.findAllBy()).isEqualTo(actualResult);
    }
}