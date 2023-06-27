package ru.practicum.shareit.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.service.PatternsApp;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ResponseError {
    private final HttpStatus status;
    private final String error;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternsApp.DATE_TIME)
    private final LocalDateTime time = LocalDateTime.now();

    public ResponseError(String error, HttpStatus status) {
        this.error = error;
        this.status = status;
    }
}