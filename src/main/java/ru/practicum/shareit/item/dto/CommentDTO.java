package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.service.CreateInfo;

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
    private LocalDateTime created;
}