package com.jts.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/email")
public class sendTestEmail {

    public static final Logger logger = LoggerFactory.getLogger(sendTestEmail.class);

    @Autowired
    private JavaMailSender mailSender;

    // Endpoint for sending test email
    @PostMapping("/testEmail")
    public ResponseEntity<String> testEmail() {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // Set recipient email and message content
            helper.setTo("pg67676@uga.edu"); // Update email accordingly
            helper.setSubject("Test Email");
            helper.setText("This is a test email from the movie-ticket-booking system.", true);

            // Send email
            mailSender.send(mimeMessage);

            logger.info("Test email sent successfully to {}", "pg67676@uga.edu");
            return ResponseEntity.ok("Test email sent successfully.");
        } catch (MessagingException e) {
            logger.error("Failed to send test email", e);
            return new ResponseEntity<>("Failed to send email.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("An unexpected error occurred", e);
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
