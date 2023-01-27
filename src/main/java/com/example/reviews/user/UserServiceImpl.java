package com.example.reviews.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private final Pattern PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    @Value("${app.secret-token}")
    private String secret = "";

    @Value("${app.token-time}")
    private int time;

    @Override
    public boolean saveUser(UserEntity userEntity) {
        if (userRepository.findByEmail(userEntity.getEmail()).isEmpty() && isEmailValid(userEntity.getEmail())) {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userRepository.save(userEntity);
            return true;
        }
        throw new UsernameNotFoundException("Username already taken");
    }

    @Override
    public Long login(UserEntity userEntity, HttpServletResponse httpServletResponse) {
        if (userRepository.findByEmail(userEntity.getEmail()).isPresent()) {
            UserEntity user = userRepository.findByEmail(userEntity.getEmail()).get();

            if (passwordEncoder.matches(userEntity.getPassword(), user.getPassword())) {
                String access_token = createToken(user.getUsername(), user.getUserId());
                String refresh_token = createToken(user.getUsername(), user.getUserId());
                httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, access_token);
                httpServletResponse.setHeader("Refresh-Token", refresh_token);
                return user.getUserId();
            }
        }
        return 0L;
    }

    public boolean isEmailValid(String email) {
        Matcher matcher = PATTERN.matcher(email);
        return matcher.matches();
    }

    public String createToken(String subject, Long id) {
        Algorithm ALGORITHM = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + time))
                .withIssuer(id.toString())
                .sign(ALGORITHM);
    }
}
