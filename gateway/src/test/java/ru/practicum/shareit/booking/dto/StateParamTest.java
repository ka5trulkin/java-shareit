package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.booking.UnsupportedStatusException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StateParamTest {
    @Test
    void stateOf_whenStateParamIsWrong_thenThrowUnsupportedStatusException() {
        assertThatThrownBy(() -> StateParam.stateOf("wrongStateParam"))
                .isInstanceOf(UnsupportedStatusException.class);
    }
}