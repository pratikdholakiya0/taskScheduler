package org.example.taskschedulersystem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.taskschedulersystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(User user, String subject, String html){
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setTo(user.getEmail());
            helper.setSubject(subject);

            try {
                InputStream inputStream = new FileInputStream(html);
                helper.setText(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).replace("givenMail", user.getEmail()).replace("ourUser", user.getUsername()), true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            mailSender.send(mail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
