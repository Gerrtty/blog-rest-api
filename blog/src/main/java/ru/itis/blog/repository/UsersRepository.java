package ru.itis.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.blog.models.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

}

