package ru.practicum.shareit.item.dto.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentDTOMapper {
    public Comment toCommentOnCreate(BookingView booking, CommentDTO commentDTO) {
        final User author = User.builder()
                .id(booking.getBooker().getId())
                .name(booking.getBooker().getName())
                .build();
        final Item item = Item.builder()
                .id(booking.getItem().getId())
                .build();
        return Comment.builder()
                .author(author)
                .item(item)
                .text(commentDTO.getText())
                .build();
    }

    public CommentDTO fromComment(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthorName())
                .created(comment.getCreated())
                .build();
    }

    public CommentDTO fromView(CommentView view) {
        return CommentDTO.builder()
                .id(view.getId())
                .text(view.getText())
                .authorName(view.getAuthor().getName())
                .created(view.getCreated())
                .build();
    }

    public List<CommentDTO> fromViewList(Collection<CommentView> views) {
        return views.stream()
                .map(CommentDTOMapper::fromView)
                .collect(Collectors.toList());
    }
}