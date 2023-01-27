package com.example.reviews.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.frontend-url}")
    private String frontendURL;

    @Autowired
    private AuthHandlerInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontendURL)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS")
                .allowedHeaders("Content-Type", "Date", "Total-Count", "loginInfo", "Authorization", "Access-Token", "Refresh-Token", HttpHeaders.AUTHORIZATION)
                .exposedHeaders("Content-Type", "Date", "Total-Count", "loginInfo", "Authorization", "Access-Token", "Refresh-Token", HttpHeaders.AUTHORIZATION)
                .maxAge(3600);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors();
        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }



}
