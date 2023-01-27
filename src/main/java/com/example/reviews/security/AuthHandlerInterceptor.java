package com.example.reviews.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private JWTVerifierBean jwtVerifier;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!request.getRequestURI().equals("/user/login") && !request.getRequestURI().equals("/user/save") && !request.getRequestURI().equals("/error")) {
            try {
                jwtVerifier.decodeToken(request);
            }
            catch (TokenExpiredException e) {
                response.setStatus(498);
                return false;
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    public static DecodedJWT decodeToken(HttpServletRequest request, String secret) {
        Algorithm ALGORITHM = Algorithm.HMAC256(secret);
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        JWTVerifier jwtVerifier = JWT.require(ALGORITHM).build();
        return jwtVerifier.verify(accessToken);
    }
}
