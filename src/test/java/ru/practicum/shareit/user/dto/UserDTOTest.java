package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.AbstractTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDTOTest extends AbstractTest {
    @Autowired
    private JacksonTester<UserDTO> jsonTester;

    @SneakyThrows
    @Test
    void testUserDTO() {
        final UserDTO userDTO = getUserDto();
        final JsonContent<UserDTO> content = jsonTester.write(userDTO);

        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(userDTO.getId().intValue());
        assertThat(content).extractingJsonPathStringValue("$.name").isEqualTo(userDTO.getName());
        assertThat(content).extractingJsonPathStringValue("$.email").isEqualTo(userDTO.getEmail());
    }
}