package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractTest;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest extends AbstractTest {

    @Test
    void testEquals() {
        final Item item1 = getItem();
        final Item item2 = getItem();

        assertThat(item1).isEqualTo(item2);
        assertThat(item1).isEqualTo(item1);
        assertThat(item1).isNotEqualTo(null);
        assertThat(item1).isNotEqualTo(getUser());
    }

    @Test
    void getIdOwner() {
        final Item item = getItem();

        assertThat(item.getIdOwner()).isEqualTo(idOne);
    }

    @Test
    void getRequestsId() {
        final Item item = getItem();

        assertThat(item.getRequestsId()).isEqualTo(idOne);

        item.getRequest().setId(null);

        assertThat(item.getRequestsId()).isNull();
    }

    @Test
    void testHashCode() {
        final Item item = getItem();

        assertThat(item.hashCode()).isEqualTo(item.hashCode());
    }
}