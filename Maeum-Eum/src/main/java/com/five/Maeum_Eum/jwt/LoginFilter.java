package com.five.Maeum_Eum.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.five.Maeum_Eum.dto.user.login.request.UserLoginDTO;
import com.five.Maeum_Eum.repository.caregiver.CaregiverRepository;
import com.five.Maeum_Eum.repository.manager.ManagerRepository;
import com.five.Maeum_Eum.service.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final ManagerRepository managerRepository;
    private final CaregiverRepository caregiverRepository;

    {
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        setUsernameParameter("id");
        UserLoginDTO user;
        try {
            user = (new ObjectMapper()).readValue(request.getInputStream(), UserLoginDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword());

        return authenticationManager.authenticate(token);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String role = authResult.getAuthorities().iterator().next().getAuthority();
        String id = null;

        if (role.equals("ROLE_MANAGER")) {
            ManagerUserDetails details = (ManagerUserDetails) authResult.getPrincipal();
            id = details.getUsername();
        }
        else {
            CaregiverUserDetails details = (CaregiverUserDetails) authResult.getPrincipal();
            id = details.getUsername();
        }

        tokenService.issueToken(response,id, role);
        tokenService.deleteRefreshTokenByDate();
        response.setStatus(200);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        response.setStatus(400);
        response.getWriter().write("{\n"
                + "\t\"status\": 401,\n"
                + "\t\"error\": \"InvalidInput\",\n"
                + "\t\"message\": \"아이디 혹은 비밀번호가 잘못되었습니다\"\n}");
    }
}
