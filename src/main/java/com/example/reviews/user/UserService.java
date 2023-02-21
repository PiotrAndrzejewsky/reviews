package com.example.reviews.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    boolean saveUser(UserEntity userEntity);
    Long login(UserEntity userEntity, HttpServletResponse httpServletResponse);
    boolean sendActivationCode(UserEntity user);
    boolean activate(String email, HttpServletRequest request);

}
