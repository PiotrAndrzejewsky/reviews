package com.example.reviews.user;

import com.example.reviews.ApiError.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Object> login(@RequestBody UserEntity userEntity, HttpServletResponse httpServletResponse) {
        Long id = userService.login(userEntity, httpServletResponse);
        if (id != 0) {
            return new ResponseEntity<Object>(id, new HttpHeaders(), HttpStatus.OK);
        }
        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MESSAGE, ApiError.DEFAULT_SUGGESTED_ACTION);
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @PostMapping("/user/activate")
    public ResponseEntity<Object> activate(HttpServletRequest request) {
        if (userService.activate(request)) {
            return new ResponseEntity<Object>(null, new HttpHeaders(), HttpStatus.OK);
        }
        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MESSAGE, ApiError.DEFAULT_SUGGESTED_ACTION);
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @PostMapping("/user/resend-activation-code")
    public ResponseEntity<Object> resendActivationCode(@RequestBody UserEntity userEntity) {
        if (userService.sendActivationCode(userEntity)) {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.OK);
        }
        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ApiError.DEFAULT_MESSAGE, ApiError.DEFAULT_SUGGESTED_ACTION);
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
}
