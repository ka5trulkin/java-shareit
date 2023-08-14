package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTOMapper;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.item.dto.item.ItemDTOMapper;
import ru.practicum.shareit.item.dto.item.ItemOut;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.utils.PatternsApp.DATE_TIME;

@JsonTest
class ItemOutTest extends AbstractTest {
    @Autowired
    private JacksonTester<ItemOut> jacksonTester;

    @SneakyThrows
    @Test
    void testItemOut() {
        final ItemDTO itemDto = getItemDto();
        final BookingView lastBooking = getBookingView();
        final BookingView nextBooking = getBookingView();
        final CommentDTO commentDTO = CommentDTOMapper.fromComment(getComment());
        final List<CommentDTO> comments = List.of(commentDTO);
        final ItemOut itemOut = ItemDTOMapper.fromBookingView(itemDto, lastBooking, nextBooking, comments);
        final JsonContent<ItemOut> content = jacksonTester.write(itemOut);

        assertThat(content).extractingJsonPathNumberValue("$.id")
                .isEqualTo(itemOut.getId().intValue());
        assertThat(content).extractingJsonPathNumberValue("$.ownerId")
                .isEqualTo(itemOut.getOwnerId().intValue());
        assertThat(content).extractingJsonPathStringValue("$.name")
                .isEqualTo(itemOut.getName());
        assertThat(content).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemOut.getDescription());
        assertThat(content).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemOut.getAvailable());
        assertThat(content).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(itemOut.getRequestId().intValue());
        assertThat(content).extractingJsonPathNumberValue("$.lastBooking.id")
                .isEqualTo(itemOut.getLastBooking().getId().intValue());
        assertThat(content).extractingJsonPathNumberValue("$.lastBooking.bookerId")
                .isEqualTo(itemOut.getLastBooking().getBookerId().intValue());
        assertThat(content).extractingJsonPathStringValue("$.lastBooking.start")
                .isEqualTo(itemOut.getLastBooking().getStart().format(DateTimeFormatter.ofPattern(DATE_TIME)));
        assertThat(content).extractingJsonPathStringValue("$.lastBooking.end")
                .isEqualTo(itemOut.getLastBooking().getEnd().format(DateTimeFormatter.ofPattern(DATE_TIME)));
        assertThat(content).extractingJsonPathNumberValue("$.nextBooking.id")
                .isEqualTo(itemOut.getNextBooking().getId().intValue());
        assertThat(content).extractingJsonPathNumberValue("$.nextBooking.bookerId")
                .isEqualTo(itemOut.getNextBooking().getBookerId().intValue());
        assertThat(content).extractingJsonPathStringValue("$.nextBooking.start")
                .isEqualTo(itemOut.getNextBooking().getStart().format(DateTimeFormatter.ofPattern(DATE_TIME)));
        assertThat(content).extractingJsonPathStringValue("$.nextBooking.end")
                .isEqualTo(itemOut.getNextBooking().getEnd().format(DateTimeFormatter.ofPattern(DATE_TIME)));
        assertThat(content).extractingJsonPathNumberValue("$.comments[0].id")
                .isEqualTo(itemOut.getComments().get(0).getId().intValue());
        assertThat(content).extractingJsonPathStringValue("$.comments[0].text")
                .isEqualTo(itemOut.getComments().get(0).getText());
        assertThat(content).extractingJsonPathStringValue("$.comments[0].authorName")
                .isEqualTo(itemOut.getComments().get(0).getAuthorName());
        assertThat(content).extractingJsonPathStringValue("$.comments[0].created")
                .isEqualTo(itemOut.getComments().get(0).getCreated().toString());
    }
}