package com.backend.elearning.domain.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

   private final JavaMailSenderImpl mailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public MailService(JavaMailSenderImpl javaMailSender) {
        this.mailSender = javaMailSender;
    }

    public void sendEmail(String recipient, String body, String subject) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true, "UTF-8");
        helper.setFrom(sender);
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(message);
    }
}
