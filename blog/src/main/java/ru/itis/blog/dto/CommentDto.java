package ru.itis.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.blog.models.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long postId;
    private String text;
    private String author;

    public static CommentDto from(Comment comment) {

        return CommentDto.builder()
                .postId(comment.getPostId())
                .author(comment.getAuthor().getEmail())
                .text(comment.getText())
                .build();
    }

    public static List<CommentDto> from(List<Comment> comments) {
        return comments.stream()
                .map(CommentDto::from)
                .collect(Collectors.toList());
    }
}
