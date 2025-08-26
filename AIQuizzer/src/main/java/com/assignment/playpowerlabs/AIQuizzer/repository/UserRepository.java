package com.assignment.playpowerlabs.AIQuizzer.repository;

import com.assignment.playpowerlabs.AIQuizzer.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
