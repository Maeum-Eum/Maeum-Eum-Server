package com.five.Maeum_Eum.repository.token;

import com.five.Maeum_Eum.entity.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByRefresh(String refresh);

    void deleteByExpirationBefore(LocalDateTime expiration);

    boolean existsByRefresh(String refresh);
}
