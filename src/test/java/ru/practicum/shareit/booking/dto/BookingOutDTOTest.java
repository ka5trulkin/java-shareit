package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.AbstractTest;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.utils.PatternsApp.DATE_TIME;

@JsonTest
class BookingOutDTOTest extends AbstractTest {
    @Autowired
    private JacksonTester<BookingOutDTO> jacksonTester;

    @SneakyThrows
    @Test
    void testBookingOutDto() {
        final BookingOutDTO expected = getBookingOutDTO();
        final JsonContent<BookingOutDTO> content = jacksonTester.write(expected);

        assertThat(content).extractingJsonPathNumberValue("$.id")
                .isEqualTo(expected.getId().intValue());
        assertThat(content).extractingJsonPathStringValue("$.start")
                .isEqualTo(expected.getStart().format(DateTimeFormatter.ofPattern(DATE_TIME)));
        assertThat(content).extractingJsonPathStringValue("$.end")
                .isEqualTo(expected.getEnd().format(DateTimeFormatter.ofPattern(DATE_TIME)));
        assertThat(content).extractingJsonPathStringValue("$.status")
                .isEqualTo(expected.getStatus().name());
        assertThat(content).extractingJsonPathNumberValue("$.booker.id")
                .isEqualTo(expected.getBooker().getId().intValue());
        assertThat(content).extractingJsonPathNumberValue("$.item.id")
                .isEqualTo(expected.getItem().getId().intValue());
        assertThat(content).extractingJsonPathStringValue("$.item.name")
                .isEqualTo(expected.getItem().getName());
    }
}