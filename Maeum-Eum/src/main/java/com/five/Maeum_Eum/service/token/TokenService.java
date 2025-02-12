package com.five.Maeum_Eum.service.token;

import com.five.Maeum_Eum.entity.token.RefreshToken;
import com.five.Maeum_Eum.jwt.JWTUtil;
import com.five.Maeum_Eum.repository.token.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTUtil jwtUtil;

    public void issueToken(HttpServletResponse response, String id, String role) {

        String access = jwtUtil.createJwt(id, "access", role,1000 * 60 * 10L);
        String refresh = jwtUtil.createJwt(id, "refresh", role,1000 * 60 * 60 * 24L);

        response.addHeader("Authorization", "Bearer " + access);
        response.addHeader("X-Refresh-Token", refresh);

        RefreshToken token = RefreshToken.builder()
                .expiration(jwtUtil.getExpiration(refresh))
                .refresh(refresh)
                .loginId(id).build();

        refreshTokenRepository.save(token);


    }

    public boolean validateRefreshToken(HttpServletResponse response ,String refresh) throws IOException {
        if (refresh == null) {
            response.getWriter().write("{ \"message\": \"REFRESH 토큰이 없습니다\" }");
            return false;
        }
        if (!refreshTokenRepository.existsByRefresh(refresh)) {
            response.getWriter().write("{ \"message\": \"존재하지 않는 REFRESH 토큰입니다\" }");
            return false;
        }
        try {
            jwtUtil.isExpired(refresh);
        } catch (Exception e) {
            refreshTokenRepository.deleteByRefresh(refresh);
            response.getWriter().write("{ \"message\": \"REFRESH 토큰이 만료되었습니다\" }");
            return false;
        }
        if (!jwtUtil.getCategory(refresh).equals("refresh")) {
            response.getWriter().write("{ \"message\": \"유효한 REFRESH 토큰이 아닙니다\" }");
            return false;
        }


        return true;
    }

    public void deleteRefreshToken(String refresh) {
        refreshTokenRepository.deleteByRefresh(refresh);
    }
    public void deleteRefreshTokenByDate() {
        refreshTokenRepository.deleteByExpirationBefore(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime());
    }

}
