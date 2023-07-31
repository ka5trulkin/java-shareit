package ru.practicum.shareit.exception.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exception.base.DataConflictException;
import ru.practicum.shareit.exception.base.NotFoundException;
import ru.practicum.shareit.exception.base.RequestException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {
    @Mock
    private ResponseError responseError;
    @InjectMocks
    private ErrorHandler errorHandler;
    private final String errorMessage = "errorMessage";

    private ResponseError getResponseError(HttpStatus status) {
        return new ResponseError(errorMessage, status);
    }

    @Test
    void badRequest() {
        final ResponseError actual = errorHandler.badRequest(new RequestException(errorMessage));

        assertThat(actual.getError()).isEqualTo(errorMessage);
        assertThat(actual.getStatus()).isEqualTo(BAD_REQUEST);
    }

    @Test
    void notFound() {
        final ResponseError actual = errorHandler.notFound(new NotFoundException(errorMessage));

        assertThat(actual.getError()).isEqualTo(errorMessage);
        assertThat(actual.getStatus()).isEqualTo(NOT_FOUND);
    }

    @Test
    void dataConflict() {
        final ResponseError actual = errorHandler.dataConflict(new DataConflictException(errorMessage));

        assertThat(actual.getError()).isEqualTo(errorMessage);
        assertThat(actual.getStatus()).isEqualTo(CONFLICT);
    }

    @Test
    void badValidation() {
        final ResponseError actual = errorHandler.internalServerError(new Throwable(errorMessage));

        assertThat(actual.getError()).isEqualTo(errorMessage);
        assertThat(actual.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR);
    }
}