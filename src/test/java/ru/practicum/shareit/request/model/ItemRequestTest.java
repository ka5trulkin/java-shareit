package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractTest;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestTest extends AbstractTest {

    @Test
    void testEquals() {
        final ItemRequest itemRequest1 = getItemRequest();
        final ItemRequest itemRequest2 = getItemRequest();

        assertThat(itemRequest1).isEqualTo(itemRequest2);
    }
}