package com.example.spring.repositories;

import com.example.spring.models.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    void deleteByExpirationDateBefore(LocalDateTime dateTime);
    boolean existsByToken(String token);
}
