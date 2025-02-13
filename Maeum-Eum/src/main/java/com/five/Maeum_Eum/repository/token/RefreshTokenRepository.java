package com.five.Maeum_Eum.repository.token;

import com.five.Maeum_Eum.entity.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByRefresh(String refresh);

    void deleteByExpirationBefore(LocalDateTime expiration);

    boolean existsByRefresh(String refresh);
}
