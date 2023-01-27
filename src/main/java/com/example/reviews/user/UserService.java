package com.example.reviews.user;

import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    boolean saveUser(UserEntity userEntity);
    Long login(UserEntity userEntity, HttpServletResponse httpServletResponse);

}
