package com.five.Maeum_Eum.jwt;

import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.manager.Manager;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final String[] exceptURL;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        if(Arrays.asList(exceptURL).contains(url)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {

            setBody(response, "ACCESS Token is expired");
            response.setStatus(401);
            return;
        }

        if (!jwtUtil.getCategory(token).equals("access")) {
            setBody(response, "ACCESS Token is not valid");
            response.setStatus(401);
            return;
        }

        String role = jwtUtil.getRole(token);

        Authentication authentication = null;

        if (role.equals("ROLE_MANAGER")) { // 액세스 토큰으로 요양보호사인지 관리자인지 구분
            Manager manager = Manager.builder().loginId(jwtUtil.getId(token)).build();
            ManagerUserDetails details = new ManagerUserDetails(manager);
            authentication = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
        }

        else {
            Caregiver caregiver = Caregiver.builder().loginId(jwtUtil.getId(token)).build();
            CaregiverUserDetails details = new CaregiverUserDetails(caregiver);
            authentication = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private void setBody(HttpServletResponse response,String str) throws IOException {
        response.getWriter().write("{\n"
                + "\t\"status\": 401,\n"
                + "\t\"error\": \"InvalidToken\",\n"
                + "\t\"message\": \"" + str + "\"\n}");
    }

}
