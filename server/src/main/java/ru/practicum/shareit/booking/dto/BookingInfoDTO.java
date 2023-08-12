package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.utils.PatternsApp;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingInfoDTO {
    private Long id;
    private Long bookerId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternsApp.DATE_TIME)
    private LocalDateTime start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternsApp.DATE_TIME)
    private LocalDateTime end;
}