package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.service.CreateInfo;
import ru.practicum.shareit.service.PatternsApp;
import ru.practicum.shareit.validation.BookingTime;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

import static ru.practicum.shareit.service.MessagesApp.*;

@Data
@Builder
@AllArgsConstructor
@BookingTime(groups = CreateInfo.class)
public class BookingDTO {
    private Long bookerId;
    @Positive(groups = CreateInfo.class)
    private Long itemId;
    @NotNull(groups = CreateInfo.class, message = START_SHOULD_NOT_BE_BLANK)
    @FutureOrPresent(groups = CreateInfo.class, message = TIME_SHOULD_NOT_BE_PAST)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternsApp.DATE_TIME)
    private LocalDateTime start;
    @NotNull(groups = CreateInfo.class, message = END_SHOULD_NOT_BE_BLANK)
    @FutureOrPresent(groups = CreateInfo.class, message = TIME_SHOULD_NOT_BE_PAST)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternsApp.DATE_TIME)
    private LocalDateTime end;
}