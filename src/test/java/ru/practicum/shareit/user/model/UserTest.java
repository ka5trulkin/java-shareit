package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractTest;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest extends AbstractTest {

    @Test
    void testEquals() {
        final User user1 = getUser();
        final User user2 = getUser();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isEqualTo(user1);
        assertThat(user1).isNotEqualTo(null);
        assertThat(user1).isNotEqualTo(getItem());
    }

    @Test
    void testHashCode() {
        final User user = getUser();

        assertThat(user.hashCode()).isEqualTo(user.hashCode());
    }
}