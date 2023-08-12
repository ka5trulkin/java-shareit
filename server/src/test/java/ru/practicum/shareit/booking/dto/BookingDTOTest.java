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
class BookingDTOTest extends AbstractTest {
    @Autowired
    private JacksonTester<BookingDTO> jacksonTester;

    @SneakyThrows
    @Test
    void testBookingDto() {
        final BookingDTO expected = getBookingDTO();
        final JsonContent<BookingDTO> content = jacksonTester.write(expected);

        assertThat(content).extractingJsonPathNumberValue("$.bookerId")
                .isEqualTo(expected.getBookerId().intValue());
        assertThat(content).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(expected.getItemId().intValue());
        assertThat(content).extractingJsonPathStringValue("$.start")
                .isEqualTo(expected.getStart().format(DateTimeFormatter.ofPattern(DATE_TIME)));
        assertThat(content).extractingJsonPathStringValue("$.end")
                .isEqualTo(expected.getEnd().format(DateTimeFormatter.ofPattern(DATE_TIME)));
    }
}