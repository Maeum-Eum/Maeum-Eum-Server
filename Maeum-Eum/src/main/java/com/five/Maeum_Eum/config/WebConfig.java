package com.five.Maeum_Eum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.server.url}")
    private String apiUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) { // 스프링단에서 cors 설정
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:", apiUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "FETCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("Authorization", "X-Refresh-Token", "Access-Control-Allow-Origin")
        ;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
