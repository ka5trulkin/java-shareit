package ru.practicum.shareit.exception.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ResponseError {
    private final HttpStatus status;
    private final String error;
    private final LocalDateTime time = LocalDateTime.now();

    public ResponseError(String error, HttpStatus status) {
        this.error = error;
        this.status = status;
    }
}