package com.interswitch.Unsolorockets.service.impl;

import com.interswitch.Unsolorockets.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.mail.host=smtp.gmail.com",
        "spring.mail.port=465",
        "spring.mail.username=",//use unsolo email address
        "spring.mail.password="//use unsolo email password
})
public class EmailMailServiceImplTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        // Configure the JavaMailSender to use a mock SMTP server
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(465);

        Properties properties = mailSender.getJavaMailProperties();
        properties.setProperty("spring.mail.properties.mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "false");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.smtp.starttls.required", "true");

    }

    @Test
    void testSendMail() throws IOException {

        String receiverEmail = "";//type an email address
        String subject = "Test Subject";
        String emailBody = "Test Email Body";
        String contentType = "context/html";

        emailService.sendMail(receiverEmail, subject, emailBody, contentType);

        assertDoesNotThrow(() -> emailService.sendMail(receiverEmail, subject, emailBody, contentType));
    }
}
