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
        assertThat(itemRequest1).isEqualTo(itemRequest1);
        assertThat(itemRequest1).isNotEqualTo(null);
        assertThat(itemRequest1).isNotEqualTo(getUser());
    }

    @Test
    void testHashCode() {
        final ItemRequest itemRequest = getItemRequest();

        assertThat(itemRequest.hashCode()).isEqualTo(itemRequest.hashCode());
    }
}