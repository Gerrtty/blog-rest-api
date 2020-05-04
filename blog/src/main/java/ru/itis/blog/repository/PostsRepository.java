package ru.itis.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.itis.blog.models.Post;

import java.util.List;

public interface PostsRepository extends JpaRepository<Post, Long> {

    Page<Post> findByAuthorId(Long authorId, Pageable pageable);

    Page<Post> findAll(Pageable pageable);

    List<Post> findAllByAuthorId(Long authorId);

    void deleteById(Long id);

    @Modifying
    @Query("update Post p set p.title = ?1 where p.id = ?2")
    void updateTitleByPostId(String title, Long id);

    @Modifying
    @Query("update Post p set p.body = ?1 where p.id = ?2")
    void updateBodyByPostId(String body, Long id);

    @Modifying
    @Query("update Post p set p.title = ?1, p.body = ?2 where p.id = ?3")
    void updateTitleAndBodyByPostId(String title, String body, Long id);

}
