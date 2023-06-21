package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private final String urlPath = "/users";
    private User validUser;
    private User testUser;
    private UserDto userDto;
    private String userId;

    @BeforeEach
    void beforeEach() {
        validUser = User.builder()
                .email("email@new.org")
                .name("Валерий Кукушкин")
                .build();
        testUser = User.builder()
                .email("valid@email.tut")
                .name("Valid Name")
                .build();
        userDto = UserDto.builder()
                .email("updated@email.tut")
                .name("Updated Name")
                .build();
        userId = "1";
    }

    @Test
    void addNewUser() throws Exception {
        // должна быть ошибка некорректного Email
        testUser.setEmail("invalid@@email.tut");
        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());
        // должна быть ошибка пустого Email
        testUser.setEmail("");
        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());
        // должна быть ошибка пустого Name
        testUser.setEmail(validUser.getEmail());
        testUser.setName("");
        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());
        // должен быть добавлен новый пользователь
        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("email@new.org"))
                .andExpect(jsonPath("$.name").value("Валерий Кукушкин"));
        // должен быть ошибка существующего Email
        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateUser() throws Exception {
        // добавление пользователя
        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("email@new.org"))
                .andExpect(jsonPath("$.name").value("Валерий Кукушкин"));
        // обновление пользователя
        mockMvc.perform(patch(String.join("/", urlPath, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("updated@email.tut"))
                .andExpect(jsonPath("$.name").value("Updated Name"));
        // добавление пользователя после обновления
        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.email").value("email@new.org"))
                .andExpect(jsonPath("$.name").value("Валерий Кукушкин"));
        // обновление имени у пользователя
        userDto.setEmail(null);
        userDto.setName("New name");
        mockMvc.perform(patch(String.join("/", urlPath, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("updated@email.tut"))
                .andExpect(jsonPath("$.name").value("New name"));
        // обновление email у пользователя
        userDto.setEmail("new@email.kek");
        userDto.setName(null);
        mockMvc.perform(patch(String.join("/", urlPath, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("new@email.kek"))
                .andExpect(jsonPath("$.name").value("New name"));
        // обновление пользователя с одинаковым email
        userDto.setEmail("new@email.kek");
        userDto.setName(null);
        mockMvc.perform(patch(String.join("/", urlPath, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("new@email.kek"))
                .andExpect(jsonPath("$.name").value("New name"));
        // должна быть ошибка обновления пользователя с уже существующим email
        userDto.setEmail(validUser.getEmail());
        userDto.setName(null);
        mockMvc.perform(patch(String.join("/", urlPath, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict());
        // получение обновленного пользователя
        mockMvc.perform(get(String.join("/", urlPath, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("new@email.kek"))
                .andExpect(jsonPath("$.name").value("New name"));
    }

    @Test
    void getUser() throws Exception {
        // должна быть ошибка получения несуществующего пользователя
        mockMvc.perform(get(String.join("/", urlPath, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());
        // добавление пользователя
        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("email@new.org"))
                .andExpect(jsonPath("$.name").value("Валерий Кукушкин"));
        // получение пользователя
        mockMvc.perform(get(String.join("/", urlPath, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("email@new.org"))
                .andExpect(jsonPath("$.name").value("Валерий Кукушкин"));
    }

    @Test
    void deleteUser() throws Exception {
        // должна быть ошибка удаления несуществующего пользователя
        mockMvc.perform(delete(String.join("/", urlPath, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());
        // добавление пользователя
        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("email@new.org"))
                .andExpect(jsonPath("$.name").value("Валерий Кукушкин"));
        // удаление пользователя
        mockMvc.perform(delete(String.join("/", urlPath, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
        // должна быть ошибка удаления несуществующего пользователя
        mockMvc.perform(delete(String.join("/", urlPath, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsers() throws Exception {
        // должен быть пустой список
        mockMvc.perform(get(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
        // добавление первого пользователя
        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("email@new.org"))
                .andExpect(jsonPath("$.name").value("Валерий Кукушкин"));
        // должен быть список с длинной 1
        mockMvc.perform(get(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));
        // добавление второго пользователя
        mockMvc.perform(post(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.email").value("valid@email.tut"))
                .andExpect(jsonPath("$.name").value("Valid Name"));
        // должен быть список с длинной 2
        mockMvc.perform(get(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
        // удаление пользователя
        mockMvc.perform(delete(String.join("/", urlPath, userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
        // должен быть список с длинной 1
        mockMvc.perform(get(urlPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}