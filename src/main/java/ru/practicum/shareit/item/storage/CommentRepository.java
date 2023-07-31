package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.comment.CommentView;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<CommentView> findByItem_Id(Long id);

    List<CommentView> findByItem_IdIn(Collection<Long> ids);
}