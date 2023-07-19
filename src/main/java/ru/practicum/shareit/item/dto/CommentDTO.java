package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.service.CreateInfo;
import ru.practicum.shareit.service.PatternsApp;

import javax.validation.constraints.NotBlank;

import java.time.LocalDateTime;

import static ru.practicum.shareit.service.MessagesApp.TEXT_SHOULD_NOT_BE_BLANK;

@Data
@Builder
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    @NotBlank(groups = CreateInfo.class, message = TEXT_SHOULD_NOT_BE_BLANK)
    private String text;
    private String authorName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternsApp.DATE_TIME_MS)
    private LocalDateTime created;
}