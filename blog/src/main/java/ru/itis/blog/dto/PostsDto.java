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
public class PostsDto {

    private Long postId;
    private String title;
    private String body;

    private String author;

    private List<CommentDto> comments;

    public static PostsDto from(Post post) {

        return PostsDto.builder()
                .author(post.getAuthor().getEmail())
                .title(post.getTitle())
                .body(post.getBody())
                .postId(post.getId())
                .build();
    }

    public static List<PostsDto> from(List<Post> posts) {
        return posts.stream()
                .map(PostsDto::from)
                .collect(Collectors.toList());
    }

}

