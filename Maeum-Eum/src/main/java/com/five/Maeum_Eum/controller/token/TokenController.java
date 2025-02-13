package com.five.Maeum_Eum.controller.token;

import com.five.Maeum_Eum.exception.ErrorResponse;
import com.five.Maeum_Eum.jwt.JWTUtil;
import com.five.Maeum_Eum.service.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final JWTUtil jwtUtil;
    private final TokenService tokenService;

    @PostMapping("/api/reissue")
    public ResponseEntity<Object> reissueToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refresh = request.getHeader("X-Refresh-Token");
        String role = jwtUtil.getRole(refresh);

        if (!tokenService.validateRefreshToken(response,refresh)) {
            return ResponseEntity
                    .status(400)
                    .body(ErrorResponse.builder()
                            .code("InvalidRequest")
                            .status(HttpStatus.BAD_REQUEST)
                            .message("유효하지 않은 refresh 토큰입니다")
                            .build());
        }

        tokenService.deleteRefreshToken(refresh);
        tokenService.issueToken(response, jwtUtil.getId(refresh), role);
        return ResponseEntity.ok().build();
    }
}
