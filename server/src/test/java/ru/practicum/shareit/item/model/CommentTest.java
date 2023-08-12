package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractTest;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest extends AbstractTest {

    @Test
    void getAuthorName() {
        final Comment comment = getComment();

        assertThat(comment.getAuthorName()).isEqualTo(name);
    }

    @Test
    void testEquals() {
        final Comment comment1 = getComment();
        final Comment comment2 = getComment();

        assertThat(comment1).isEqualTo(comment2);
        assertThat(comment1).isEqualTo(comment1);
        assertThat(comment1).isNotEqualTo(null);
        assertThat(comment1).isNotEqualTo(getUser());
    }

    @Test
    void testHashCode() {
        final Comment comment = getComment();

        assertThat(comment.hashCode()).isEqualTo(comment.hashCode());
    }
}