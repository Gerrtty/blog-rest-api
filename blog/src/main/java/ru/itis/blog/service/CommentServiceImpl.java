package ru.itis.blog.service;

import org.springframework.stereotype.Service;
import ru.itis.blog.dto.CommentDto;
import ru.itis.blog.models.Comment;
import ru.itis.blog.models.Post;
import ru.itis.blog.models.User;
import ru.itis.blog.repository.CommentsRepository;
import ru.itis.blog.repository.UsersRepository;

@Service
public class CommentServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final UsersRepository usersRepository;

    public CommentServiceImpl(CommentsRepository commentsRepository,
                              UsersRepository usersRepository) {
        this.commentsRepository = commentsRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public CommentDto create(Long postId, Long authorId, String text) {

        Comment comment = Comment.builder()
                .text(text)
                .author(usersRepository.getOne(authorId))
                .postId(postId)
                .build();

        return CommentDto.from(commentsRepository.save(comment));

    }
}
