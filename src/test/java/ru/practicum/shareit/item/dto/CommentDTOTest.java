package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTOMapper;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.utils.PatternsApp.DATE_TIME_MS;

@JsonTest
class CommentDTOTest extends AbstractTest {
    @Autowired
    private JacksonTester<CommentDTO> jacksonTester;

    @SneakyThrows
    @Test
    void testCommentDTO() {
        final CommentDTO commentDTO = CommentDTOMapper.fromComment(getComment());
        final JsonContent<CommentDTO> content = jacksonTester.write(commentDTO);

        assertThat(content).extractingJsonPathNumberValue("$.id")
                .isEqualTo(commentDTO.getId().intValue());
        assertThat(content).extractingJsonPathStringValue("$.text")
                .isEqualTo(commentDTO.getText());
        assertThat(content).extractingJsonPathStringValue("$.authorName")
                .isEqualTo(commentDTO.getAuthorName());
        assertThat(content).extractingJsonPathStringValue("$.created")
                .isEqualTo(commentDTO.getCreated().format(DateTimeFormatter.ofPattern(DATE_TIME_MS)));
    }
}