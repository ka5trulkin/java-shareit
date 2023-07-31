package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.item.dto.item.ItemInRequest;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.utils.PatternsApp.DATE_TIME;

@JsonTest
class ItemRequestOutWithItemsTest extends AbstractTest {
    @Autowired
    private JacksonTester<ItemRequestOutWithItems> jsonTester;

    @SneakyThrows
    @Test
    void testItemRequestCreate() {
        final ItemInRequest itemInRequest = new ItemInRequest(idOne, name, description, true, idOne);
        final ItemRequestOutWithItems requestDto =
                new ItemRequestOutWithItems(idOne, description, dateTime, List.of(itemInRequest));
        final JsonContent<ItemRequestOutWithItems> content = jsonTester.write(requestDto);

        assertThat(content).extractingJsonPathNumberValue("$.id")
                .isEqualTo(requestDto.getId().intValue());
        assertThat(content).extractingJsonPathStringValue("$.description")
                .isEqualTo(requestDto.getDescription());
        assertThat(content).extractingJsonPathStringValue("$.created")
                .isEqualTo(requestDto.getCreated().format(DateTimeFormatter.ofPattern(DATE_TIME)));
        assertThat(content).extractingJsonPathNumberValue("$.items[0].id")
                .isEqualTo(requestDto.getItems().get(0).getId().intValue());
        assertThat(content).extractingJsonPathStringValue("$.items[0].name")
                .isEqualTo(requestDto.getItems().get(0).getName());
        assertThat(content).extractingJsonPathStringValue("$.items[0].description")
                .isEqualTo(requestDto.getItems().get(0).getDescription());
        assertThat(content).extractingJsonPathBooleanValue("$.items[0].available")
                .isEqualTo(requestDto.getItems().get(0).getAvailable());
        assertThat(content).extractingJsonPathNumberValue("$.items[0].requestId")
                .isEqualTo(requestDto.getItems().get(0).getRequestId().intValue());
    }
}