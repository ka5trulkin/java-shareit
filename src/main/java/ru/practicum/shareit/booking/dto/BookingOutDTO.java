package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemIdAndNameDTO;
import ru.practicum.shareit.service.PatternsApp;
import ru.practicum.shareit.user.dto.UserIdDTO;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingOutDTO {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternsApp.DATE_TIME)
    private LocalDateTime start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternsApp.DATE_TIME)
    private LocalDateTime end;
    private Status status;
    private UserIdDTO booker;
    private ItemIdAndNameDTO item;
}