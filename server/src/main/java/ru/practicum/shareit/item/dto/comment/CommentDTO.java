package ru.practicum.shareit.item.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.utils.PatternsApp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String text;
    private String authorName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternsApp.DATE_TIME_MS)
    private LocalDateTime created;
}