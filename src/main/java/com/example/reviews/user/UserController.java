package com.example.reviews.user;

import com.example.reviews.ApiError.ApiError;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/save")
    public ResponseEntity<Object> signUpUsername(@RequestBody UserEntity userEntity) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(userEntity));
    }

    @PostMapping("/user/login")
    public ResponseEntity<Object> login(@RequestBody UserEntity userEntity, HttpServletResponse httpServletResponse) { // take request body from request (username, password)
        Long id = userService.login(userEntity, httpServletResponse);
        if (id != 0) {
            return new ResponseEntity<Object>(id, new HttpHeaders(), HttpStatus.OK);
        }
        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MESSAGE, ApiError.DEFAULT_SUGGESTED_ACTION);
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
}
