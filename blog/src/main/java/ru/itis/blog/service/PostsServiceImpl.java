package ru.itis.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.itis.blog.dto.CommentDto;
import ru.itis.blog.dto.PostsDto;
import ru.itis.blog.dto.RepostDto;
import ru.itis.blog.dto.ResponsePostDto;
import ru.itis.blog.models.Post;
import ru.itis.blog.models.User;
import ru.itis.blog.repository.CommentsRepository;
import ru.itis.blog.repository.PostsRepository;


import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;

    public PostsServiceImpl(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    @Autowired
    private CommentsRepository commentsRepository;


    @Override
    public List<ResponsePostDto<?>> getAllPosts(Integer page) {

        List<Post> list = postsRepository.findAll(PageRequest.of(page, 5)).getContent();
        List<Post> posts;

        if(list.size() < 5 && page < 2) {
            posts = postsRepository.findAll();
        }

        else posts = postsRepository.findAll(PageRequest.of(page, 5)).getContent();

        return getPostsDto(posts);

    }

    private List<ResponsePostDto<?>> getPostsDto(List<Post> posts) {

        List<ResponsePostDto<?>> responsePostDtos = new ArrayList<>();

        for (Post post : posts) {
            if(post.getForwarded() == null) {
                PostsDto postsDto = PostsDto.from(post);
                postsDto.setComments(CommentDto.from(commentsRepository.findAllByPostId(post.getId())));
                responsePostDtos.add(new ResponsePostDto<>(postsDto));
            }
            else {
                RepostDto repostDto = RepostDto.from(post);
                repostDto.setComments(CommentDto.from(commentsRepository.findAllByPostId(post.getId())));
                responsePostDtos.add(new ResponsePostDto<>(repostDto));
            }
        }

        return responsePostDtos;
    }

    @Override
    public List<ResponsePostDto<?>> getUserPosts(Long userId, Integer page) {

        List<Post> list = postsRepository.findByAuthorId(userId, PageRequest.of(page, 5)).getContent();
        List<Post> posts;

        if(list.size() < 5 && page < 2) {
            posts = postsRepository.findAllByAuthorId(userId);
        }

        else posts = postsRepository.findByAuthorId(userId, PageRequest.of(page, 5)).getContent();

        return getPostsDto(posts);
    }

    @Override
    public ResponsePostDto<?> get(Long id) {

        Post post = postsRepository.getOne(id);
        List<CommentDto> comments = CommentDto.from(commentsRepository.findAllByPostId(id));

        if(post.getForwarded() == null) {
            PostsDto postsDto = PostsDto.from(post);
            postsDto.setComments(comments);
            return new ResponsePostDto<>(postsDto);
        }

        else {
            RepostDto repostDto = RepostDto.from(post);
            repostDto.setComments(comments);
            return new ResponsePostDto<>(repostDto);
        }

    }

    @Override
    public void delete(Long id, Long authUserId) throws AccessDeniedException {

        if(postsRepository.getOne(id).getAuthor().getId().equals(authUserId)) {
            postsRepository.deleteById(id);
        }

        else throw new AccessDeniedException("You can't delete this post because you not author");
    }

    @Override
    public Post updateTitle(String title, Long postId) {
        postsRepository.updateTitleByPostId(title, postId);
        return postsRepository.getOne(postId);
    }

    @Override
    public Post updateBody(String body, Long postId) {
        postsRepository.updateBodyByPostId(body, postId);
        return postsRepository.getOne(postId);
    }

    @Override
    public Post updateTitleAndBody(String title, String body, Long postId) {
        postsRepository.updateTitleAndBodyByPostId(title, body, postId);
        return postsRepository.getOne(postId);
    }

    @Override
    public Post update(PostsDto postsDto, Long postId, Long authUserId) throws AccessDeniedException {

        if(postsRepository.getOne(postId).getAuthor().getId().equals(authUserId)) {

            if(postsDto.getTitle() != null && postsDto.getBody() == null) {
                return updateTitle(postsDto.getTitle(), postId);
            }

            else if(postsDto.getTitle() != null && postsDto.getBody() != null) {
                return updateTitleAndBody(postsDto.getTitle(), postsDto.getBody(), postId);
            }

            else if(postsDto.getTitle() == null && postsDto.getBody() != null) {
                return updateBody(postsDto.getBody(), postId);
            }

            return postsRepository.getOne(postId);
        }

        throw new AccessDeniedException("You can't change this post because you are't author");

    }

    @Override
    public Post createRepost(User user, Long forwardedFrom) {

        Post post = postsRepository.getOne(forwardedFrom);

        Post repost = Post.builder()
                .author(user)
                .forwarded(post.getAuthor())
                .title(post.getTitle())
                .body(post.getBody())
                .build();

        return postsRepository.save(repost);
    }

    @Override
    public Post createPost(User user, PostsDto postsDto) {

        Post post = Post.builder()
                .author(user)
                .title(postsDto.getTitle())
                .body(postsDto.getBody())
                .build();

        return postsRepository.save(post);
    }

}
