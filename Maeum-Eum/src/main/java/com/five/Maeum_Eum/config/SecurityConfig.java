package com.five.Maeum_Eum.config;

import com.five.Maeum_Eum.jwt.JWTFilter;
import com.five.Maeum_Eum.jwt.JWTUtil;
import com.five.Maeum_Eum.jwt.LoginFilter;
import com.five.Maeum_Eum.jwt.LogoutFilter;
import com.five.Maeum_Eum.repository.caregiver.CaregiverRepository;
import com.five.Maeum_Eum.repository.manager.ManagerRepository;
import com.five.Maeum_Eum.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${spring.server.url}")
    private String apiUrl;
    private final AuthenticationConfiguration configuration;
    private final TokenService tokenService;
    private final JWTUtil jwtUtil;
    private final ManagerRepository managerRepository;
    private final CaregiverRepository caregiverRepository;

    @Bean
    public AuthenticationManager manager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() { // localhost와 api서버에 대해서만 cors 허용
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:", apiUrl));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "FETCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setMaxAge(3600L);
        configuration.setExposedHeaders(List.of("Authorization", "X-Refresh-Token", "Access-Control-Allow-Origin"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {

        String[] exceptURL = { // 해당 경로에 대해 액세스 토큰 검증 진행하지 않음
                "/api/login"
                ,"/api/caregiver/register"
                ,"/api/manager/register"
                ,"/api/reissue"
                ,"/api/validateID"
                ,"/api/logout"
                ,"/api/center"
        };

        security.csrf(auth -> auth.disable());
        security.formLogin(auth -> auth.disable());
        security.httpBasic(auth -> auth.disable());
        security.authorizeHttpRequests(auth -> auth
                .requestMatchers(exceptURL).permitAll()
                .anyRequest().authenticated());
        security.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        security.addFilterAt(new LoginFilter(manager(configuration), tokenService, managerRepository, caregiverRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil, exceptURL), UsernamePasswordAuthenticationFilter.class);
        security.addFilterBefore(new LogoutFilter(tokenService), org.springframework.security.web.authentication.logout.LogoutFilter.class);
        security.cors((corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource())));
        return security.build();
    }
}
