package com.hilal.newsApp.repository;

import com.hilal.newsApp.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(@NotBlank String username);

    Optional<User> findByUsername(@NotBlank String username);
}
