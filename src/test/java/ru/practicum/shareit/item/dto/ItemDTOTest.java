package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.item.dto.item.ItemDTO;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDTOTest extends AbstractTest {
    @Autowired
    private JacksonTester<ItemDTO> jacksonTester;

    @SneakyThrows
    @Test
    void testItemDto() {
        final ItemDTO itemDto = getItemDto();
        final JsonContent<ItemDTO> content = jacksonTester.write(itemDto);

        assertThat(content).extractingJsonPathNumberValue("$.id")
                .isEqualTo(itemDto.getId().intValue());
        assertThat(content).extractingJsonPathNumberValue("$.ownerId")
                .isEqualTo(itemDto.getOwnerId().intValue());
        assertThat(content).extractingJsonPathStringValue("$.name")
                .isEqualTo(itemDto.getName());
        assertThat(content).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemDto.getDescription());
        assertThat(content).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemDto.getAvailable());
        assertThat(content).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(itemDto.getRequestId().intValue());
    }
}