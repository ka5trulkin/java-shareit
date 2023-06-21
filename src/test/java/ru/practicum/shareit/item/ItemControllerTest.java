package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private final String urlItemPath = "/items";
    private final String urlUserPath = "/users";
    private User user;
    private Item validItem;
    private Item testItem;
    private ItemDto itemDto;
    private String itemId;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .email("email@new.org")
                .name("Валерий Кукушкин")
                .build();
        validItem = Item.builder()
                .ownerId(1)
                .name("Перфоратор Бошш")
                .description("Бери и е...")
                .available(true)
                .build();
        testItem = Item.builder()
                .ownerId(1)
                .name("Valid name")
                .description("Valid description")
                .available(true)
                .build();
        itemDto = ItemDto.builder()
                .name("Перфоратор Бошш")
                .description("Бери и е...")
                .available(true)
                .build();
        itemId = "1";
    }

    @Test
    void addNewItem() throws Exception {
        // должна быть ошибка пустого Name
        testItem.setName(" ");
        mockMvc.perform(post(urlItemPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isBadRequest());
        // должна быть ошибка пустого Description
        testItem.setName("Valid name");
        testItem.setDescription(" ");
        mockMvc.perform(post(urlItemPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isBadRequest());
        testItem.setDescription("Valid description");
        // должна быть добавлена новая вещь
        mockMvc.perform(post(urlUserPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("email@new.org"))
                .andExpect(jsonPath("$.name").value("Валерий Кукушкин"));
        mockMvc.perform(post(urlItemPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.ownerId").value("1"))
                .andExpect(jsonPath("$.name").value("Перфоратор Бошш"))
                .andExpect(jsonPath("$.description").value("Бери и е..."))
                .andExpect(jsonPath("$.available").value(true));
        // должна быть ошибка добавления вещи с несуществующим пользователем
        mockMvc.perform(post(urlItemPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 10)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateItem() throws Exception {
        // добавление вещи
        mockMvc.perform(post(urlUserPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("email@new.org"))
                .andExpect(jsonPath("$.name").value("Валерий Кукушкин"));
        mockMvc.perform(post(urlItemPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.ownerId").value("1"))
                .andExpect(jsonPath("$.name").value("Перфоратор Бошш"))
                .andExpect(jsonPath("$.description").value("Бери и е..."))
                .andExpect(jsonPath("$.available").value(true));
        // обновление вещи
        testItem.setAvailable(false);
        mockMvc.perform(patch(String.join("/", urlItemPath, itemId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.ownerId").value("1"))
                .andExpect(jsonPath("$.name").value("Valid name"))
                .andExpect(jsonPath("$.description").value("Valid description"))
                .andExpect(jsonPath("$.available").value(false));
        // должна быть ошибка без id пользователя
        mockMvc.perform(patch(String.join("/", urlItemPath, itemId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isBadRequest());
        // должна быть ошибка несуществующего пользователя
        mockMvc.perform(patch(String.join("/", urlItemPath, itemId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 777)
                        .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isNotFound());
        // обновление name
        testItem.setName("New name");
        testItem.setDescription(null);
        mockMvc.perform(patch(String.join("/", urlItemPath, itemId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.ownerId").value("1"))
                .andExpect(jsonPath("$.name").value("New name"))
                .andExpect(jsonPath("$.description").value("Valid description"))
                .andExpect(jsonPath("$.available").value(false));
        // обновление description
        testItem.setName(null);
        testItem.setDescription("New description");
        mockMvc.perform(patch(String.join("/", urlItemPath, itemId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.ownerId").value("1"))
                .andExpect(jsonPath("$.name").value("New name"))
                .andExpect(jsonPath("$.description").value("New description"))
                .andExpect(jsonPath("$.available").value(false));
        // обновление available
        testItem.setName(null);
        testItem.setDescription(null);
        testItem.setAvailable(true);
        mockMvc.perform(patch(String.join("/", urlItemPath, itemId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.ownerId").value("1"))
                .andExpect(jsonPath("$.name").value("New name"))
                .andExpect(jsonPath("$.description").value("New description"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void getItemById() throws Exception {
        // добавление вещи
        mockMvc.perform(post(urlUserPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("email@new.org"))
                .andExpect(jsonPath("$.name").value("Валерий Кукушкин"));
        mockMvc.perform(post(urlItemPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.ownerId").value("1"))
                .andExpect(jsonPath("$.name").value("Перфоратор Бошш"))
                .andExpect(jsonPath("$.description").value("Бери и е..."))
                .andExpect(jsonPath("$.available").value(true));
        // получение вещи
        mockMvc.perform(get(String.join("/", urlItemPath, itemId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.ownerId").value("1"))
                .andExpect(jsonPath("$.name").value("Перфоратор Бошш"))
                .andExpect(jsonPath("$.description").value("Бери и е..."))
                .andExpect(jsonPath("$.available").value(true));
        // ошибка получение вещи по несуществующему id
        String notExistItem = "777";
        mockMvc.perform(get(String.join("/", urlItemPath, notExistItem))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void getItemsByOwner() throws Exception {
        // добавление вещи
        mockMvc.perform(post(urlUserPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("email@new.org"))
                .andExpect(jsonPath("$.name").value("Валерий Кукушкин"));
        mockMvc.perform(post(urlItemPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.ownerId").value("1"))
                .andExpect(jsonPath("$.name").value("Перфоратор Бошш"))
                .andExpect(jsonPath("$.description").value("Бери и е..."))
                .andExpect(jsonPath("$.available").value(true));
        // получение вещи
        mockMvc.perform(get(urlItemPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].ownerId").value("1"))
                .andExpect(jsonPath("$[0].name").value("Перфоратор Бошш"))
                .andExpect(jsonPath("$[0].description").value("Бери и е..."))
                .andExpect(jsonPath("$[0].available").value(true));
        // ошибка получение вещи по несуществующему owner
        long notExistOwner = 777;
        mockMvc.perform(get(urlItemPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", notExistOwner))
                .andExpect(status().isNotFound());
    }

    @Test
    void getItemBySearch() throws Exception {
        // добавление вещи
        mockMvc.perform(post(urlUserPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.email").value("email@new.org"))
                .andExpect(jsonPath("$.name").value("Валерий Кукушкин"));
        mockMvc.perform(post(urlItemPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.ownerId").value("1"))
                .andExpect(jsonPath("$.name").value("Перфоратор Бошш"))
                .andExpect(jsonPath("$.description").value("Бери и е..."))
                .andExpect(jsonPath("$.available").value(true));
        // получение вещи
        mockMvc.perform(get(String.join("/", urlItemPath, "search"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "рфОр"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].ownerId").value("1"))
                .andExpect(jsonPath("$[0].name").value("Перфоратор Бошш"))
                .andExpect(jsonPath("$[0].description").value("Бери и е..."))
                .andExpect(jsonPath("$[0].available").value(true));
        // получение списка с пустым запросом
        mockMvc.perform(get(String.join("/", urlItemPath, "search"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", " "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}