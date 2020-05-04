package ru.itis.blog.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.blog.dto.CommentDto;
import ru.itis.blog.models.Comment;
import ru.itis.blog.sequrity.jwt.details.UserDetailsImpl;
import ru.itis.blog.service.CommentsService;

@RestController
public class CommentsController {

    private final CommentsService commentService;

    public CommentsController(CommentsService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/post/{postId}/createComment")
    public ResponseEntity<CommentDto> create(@PathVariable Long postId, @RequestBody CommentDto commentDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getDetails();

        return ResponseEntity.ok(commentService.create(postId, userDetails.getUserId(), commentDto.getText()));
    }

}
