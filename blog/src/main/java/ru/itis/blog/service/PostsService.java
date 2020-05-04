package ru.itis.blog.service;

import ru.itis.blog.dto.PostsDto;
import ru.itis.blog.dto.ResponsePostDto;
import ru.itis.blog.models.Post;
import ru.itis.blog.models.User;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface PostsService {

    List<ResponsePostDto<?>> getAllPosts(Integer pageable);

    List<ResponsePostDto<?>> getUserPosts(Long userId, Integer pageable);

    Post createPost(User user, PostsDto postsDto);

    ResponsePostDto<?> get(Long id);

    void delete(Long id, Long authUserId) throws AccessDeniedException;

    Post updateTitle(String title, Long postId);

    Post updateBody(String body, Long postId);

    Post updateTitleAndBody(String title, String body, Long postId);

    Post update(PostsDto postsDto, Long postId, Long authUserId) throws AccessDeniedException;

    Post createRepost(User user, Long forwardedFrom);

}
