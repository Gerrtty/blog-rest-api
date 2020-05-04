package ru.itis.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.blog.models.Post;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepostDto {

    private Long postId;
    private String forwarded_from;
    private String title;
    private String body;

    private String author;

    private List<CommentDto> comments;

    public static RepostDto from(Post post) {

        return RepostDto.builder()
                .author(post.getAuthor().getEmail())
                .title(post.getTitle())
                .body(post.getBody())
                .postId(post.getId())
                .forwarded_from(post.getForwarded().getEmail())
                .build();
    }

    public static List<RepostDto> from(List<Post> posts) {
        return posts.stream()
                .map(RepostDto::from)
                .collect(Collectors.toList());
    }

}
