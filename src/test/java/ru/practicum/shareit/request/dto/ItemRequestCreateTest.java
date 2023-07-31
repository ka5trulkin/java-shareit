package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.AbstractTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestCreateTest extends AbstractTest {
    @Autowired
    private JacksonTester<ItemRequestCreate> jsonTester;

    @SneakyThrows
    @Test
    void testItemRequestCreateT() {
        final ItemRequestCreate requestDto = new ItemRequestCreate(description);
        final JsonContent<ItemRequestCreate> content = jsonTester.write(requestDto);

        assertThat(content).extractingJsonPathStringValue("$.description")
                .isEqualTo(requestDto.getDescription());
    }
}