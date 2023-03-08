package com.example.reviews.user;

import com.example.reviews.email.EmailService;
import com.example.reviews.security.AuthHandlerInterceptor;
import com.example.reviews.security.JWTVerifierBean;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Value("${app.secret-token}")
    private String secret = "";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTVerifierBean jWTVerifierBean;

    @Autowired
    private EmailService emailService;



    @Override
    @Transactional
    public boolean saveUser(UserEntity userEntity) {
        if (userRepository.findByEmail(userEntity.getEmail()).isEmpty() && emailService.isEmailValid(userEntity.getEmail())) {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userRepository.save(userEntity);
            emailService.sendActivationCode(userEntity);
            return true;
        }
        throw new UsernameNotFoundException("Email already taken");
    }

    @Override
    public Long login(UserEntity userEntity, HttpServletResponse httpServletResponse) {
        if (userRepository.findByEmail(userEntity.getEmail()).isPresent()) {
            UserEntity user = userRepository.findByEmail(userEntity.getEmail()).get();
            if (passwordEncoder.matches(userEntity.getPassword(), user.getPassword()) && user.isActive()) {
                String access_token = jWTVerifierBean.createToken(user.getUsername(), user.getUserId().toString());
                String refresh_token = jWTVerifierBean.createToken(user.getUsername(), user.getUserId().toString());
                httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, access_token);
                httpServletResponse.setHeader("Refresh-Token", refresh_token);
                return user.getUserId();
            }
        }
        return 0L;
    }

    @Override
    public boolean sendActivationCode(UserEntity user) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(user.getEmail());
        if (userEntity.isPresent() && !userEntity.get().isActive()) {
            emailService.sendActivationCode(userEntity.get());
            return true;
        }
        return false;
    }


    @Override
    @Transactional
    public boolean activate(HttpServletRequest request) {
        Optional<UserEntity> user = userRepository.findById(Long.valueOf(AuthHandlerInterceptor.decodeToken(request, secret).getIssuer()));
        if (user.isPresent()) {
            user.get().setActive(true);
            return true;
        }
        return false;
    }
}
