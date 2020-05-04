package ru.itis.blog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Authenticator;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.itis.blog.dto.PostsDto;
import ru.itis.blog.dto.RepostDto;
import ru.itis.blog.dto.ResponsePostDto;
import ru.itis.blog.models.Post;
import ru.itis.blog.models.User;
import ru.itis.blog.sequrity.jwt.details.UserDetailsImpl;
import ru.itis.blog.service.PostsService;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
public class PostsController {

    private final PostsService postsService;

    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/post/{id}")
    public ResponseEntity<ResponsePostDto> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postsService.get(id));
    }

    // Get all posts on page
    @GetMapping("/posts/{page}")
    public ResponseEntity<List<ResponsePostDto<?>>> getAllPost(@PathVariable Integer page) {
        return ResponseEntity.ok(postsService.getAllPosts(page));
    }

    // Get all posts by user on page
    @GetMapping("/userPosts/id{userId}/{page}")
    public ResponseEntity<List<ResponsePostDto<?>>> getAllPost(@PathVariable Integer page,
                                                     @PathVariable Long userId) {
        return ResponseEntity.ok(postsService.getUserPosts(userId, page));
    }

    // Create a new post
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<PostsDto> createPost(@RequestBody PostsDto postsDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getDetails();

        return ResponseEntity.ok(PostsDto.from(postsService.createPost(User.builder()
                .id(userDetails.getUserId())
                .email(userDetails.getUsername())
                .build(), postsDto)));

    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deletePost(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getDetails();

        try {
            postsService.delete(id, userDetails.getUserId());
            return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<Error>(HttpStatus.FORBIDDEN);
        }

    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostsDto postsDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getDetails();

        try {
            Post post = postsService.update(postsDto, id, userDetails.getUserId());

            return ResponseEntity.ok(PostsDto.from(post));

        } catch (AccessDeniedException e) {
            return new ResponseEntity<Error>(HttpStatus.FORBIDDEN);
        }

    }

    // Create a new RePost
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/post/{postId}/createRePost")
    public ResponseEntity<RepostDto> createRePost(@PathVariable Long postId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getDetails();

        return ResponseEntity.ok(RepostDto.from(postsService.createRepost(User.builder()
                .id(userDetails.getUserId())
                .email(userDetails.getUsername())
                .build(), postId)));

    }

}
