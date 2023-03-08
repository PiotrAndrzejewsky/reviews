package com.example.reviews.email;

import com.example.reviews.security.JWTVerifierBean;
import com.example.reviews.user.UserEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    @Value("${app.frontend-url}")
    private String url;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendActivationCode(UserEntity user) {
        String activationCode = jWTVerifierBean.createToken(user.getUsername(), user.getUserId().toString());
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        String link = url + "/activate?token=";
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Activation Code");

            String htmlBody = "<p>Please click on the following link to activate your account:</p><p><a href=\"" + link + activationCode + "\">Activate</a></p>";
            helper.setText(htmlBody, true);
            System.out.println("dziala");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(message);
    }

    public boolean isEmailValid(String email) {
        Matcher matcher = PATTERN.matcher(email);
        return matcher.matches();
    }

}