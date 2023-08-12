package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractTest;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTOMapper;
import ru.practicum.shareit.item.dto.comment.CommentView;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommentDTOMapperTest extends AbstractTest {
    @Test
    void toCommentOnCreate() {
        final Comment expectedResult = getCommentNoId();
        final CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText(text);
        final Comment actualResult = CommentDTOMapper.toCommentOnCreate(getBookingView(), commentDTO);
        actualResult.setCreated(nowTime);

        assertThat(actualResult.getId()).isEqualTo(expectedResult.getId());
        assertThat(actualResult.getAuthor()).isInstanceOf(User.class);
        assertThat(actualResult.getAuthor().getId()).isEqualTo(expectedResult.getAuthor().getId());
        assertThat(actualResult.getAuthorName()).isEqualTo(expectedResult.getAuthorName());
        assertThat(actualResult.getItem()).isInstanceOf(Item.class);
        assertThat(actualResult.getItem().getId()).isEqualTo(expectedResult.getItem().getId());
        assertThat(actualResult.getText()).isEqualTo(expectedResult.getText());
        assertThat(actualResult.getCreated()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    void fromComment() {
        final CommentDTO expectedResult = getCommentDto();
        final CommentDTO actualResult = CommentDTOMapper.fromComment(getComment());

        assertThat(actualResult.getId()).isEqualTo(expectedResult.getId());
        assertThat(actualResult.getText()).isEqualTo(expectedResult.getText());
        assertThat(actualResult.getAuthorName()).isEqualTo(expectedResult.getAuthorName());
        assertThat(actualResult.getCreated()).isEqualTo(expectedResult.getCreated());
    }

    @Test
    void fromView() {
        final CommentDTO expectedResult = getCommentDto();
        final CommentDTO actualResult = CommentDTOMapper.fromView(getCommentView(getComment()));

        assertThat(actualResult.getId()).isEqualTo(expectedResult.getId());
        assertThat(actualResult.getText()).isEqualTo(expectedResult.getText());
        assertThat(actualResult.getAuthorName()).isEqualTo(expectedResult.getAuthorName());
        assertThat(actualResult.getCreated()).isEqualTo(expectedResult.getCreated());
    }

    @Test
    void fromViewList() {
        final CommentDTO expectedResult = getCommentDto();
        final List<CommentView> viewList = List.of(getCommentView(getComment()));
        final List<CommentDTO> actualResult = CommentDTOMapper.fromViewList(viewList);

        assertThat(actualResult).isNotEmpty().hasSize(1);
        assertThat(actualResult.get(0).getId()).isEqualTo(expectedResult.getId());
        assertThat(actualResult.get(0).getText()).isEqualTo(expectedResult.getText());
        assertThat(actualResult.get(0).getAuthorName()).isEqualTo(expectedResult.getAuthorName());
        assertThat(actualResult.get(0).getCreated()).isEqualTo(expectedResult.getCreated());
    }
}