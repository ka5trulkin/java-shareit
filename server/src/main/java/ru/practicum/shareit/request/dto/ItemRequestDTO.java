package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.utils.PatternsApp;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class ItemRequestDTO {
    private Long id;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternsApp.DATE_TIME)
    private LocalDateTime created;
}