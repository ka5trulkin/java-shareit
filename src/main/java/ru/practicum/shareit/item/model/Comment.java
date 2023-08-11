package ru.practicum.shareit.item.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;
    @Column(name = "comment_text")
    private String text;
    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;

    public String getAuthorName() {
        return author.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Comment comment = (Comment) o;
        return getId() != null && Objects.equals(getId(), comment.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
