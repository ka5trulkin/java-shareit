package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.item.ItemIdAndName;
import ru.practicum.shareit.user.dto.UserIdDTO;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingOutDTO {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private UserIdDTO booker;
    private ItemIdAndName item;
}