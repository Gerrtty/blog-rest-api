package ru.itis.blog.service;

import ru.itis.blog.dto.CommentDto;
import ru.itis.blog.models.Comment;

public interface CommentsService {

    CommentDto create(Long postId, Long authorId, String text);

}
