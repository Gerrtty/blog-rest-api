package ru.itis.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.blog.models.Comment;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(Long postId);

}
