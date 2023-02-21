package com.example.reviews.email;

import com.example.reviews.security.JWTVerifierBean;
import com.example.reviews.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailService {

    private final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private final Pattern PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
    private final JavaMailSender javaMailSender;

    @Autowired
    private JWTVerifierBean jWTVerifierBean;
    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendActivationCode(UserEntity user) {
        String activationCode = jWTVerifierBean.createToken(user.getUsername(), user.getUserId().toString());;
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());
        msg.setSubject("Activation Code");
        msg.setText("Your activation code is: " + activationCode);
        javaMailSender.send(msg);
    }

    public boolean isEmailValid(String email) {
        Matcher matcher = PATTERN.matcher(email);
        return matcher.matches();
    }

}