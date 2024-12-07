package com.jts.movie.services;



import com.jts.movie.entities.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    // Email sending logic
    private void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // HTML content
        mailSender.send(mimeMessage);
        log.info("Confirmation email sent to: {}", to);
    }


    public void sendResetPasswordEmail(String to, String resetToken) throws MessagingException {
        String subject = "Reset Your Password";
        String body = "You have requested to reset your password. Please click the link below to reset it:\n" +
                "http://localhost:4200/resetPassword?token=" + resetToken;  // Correct URL for frontend reset

        sendEmail(to, subject, body);
    }
    // Method to send promotion email
    public void sendPromotionEmail(String to, String title, String description) throws MessagingException {
        String subject = "Exclusive Promotion: " + title;
        String body = "Dear User,\n\n" +
                "We are excited to share our latest promotion with you:\n\n" +
                title + "\n\n" +
                description + "\n\n" +
                "Best regards,\nYour CineQuest Team";

        sendEmail(to, subject, body);
    }

    public void sendBookingConfirmation(String userEmail, Booking booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(userEmail);
            helper.setSubject("Booking Confirmation - CineQuest");
            helper.setText(buildEmailContent(booking), true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send booking confirmation email", e);
        }
    }

    private String buildEmailContent(Booking booking) {
        StringBuilder content = new StringBuilder();
        content.append("<h1>Booking Confirmation</h1>")
                .append("<p>Thank you for booking with CineQuest!</p>")
                .append("<p><b>Movie:</b> ").append(booking.getMovieName()).append("</p>")
                .append("<p><b>Show Date:</b> ").append(booking.getShow().getDate()).append("</p>")
                .append("<p><b>Show Time:</b> ").append(booking.getShow().getTime()).append("</p>")
                .append("<p><b>Seats:</b> ");


        booking.getShow().getBookedSeats().forEach(seat -> content.append(seat).append(" "));

        content.append("</p>")
                .append("<p><b>Total Amount:</b> $").append(booking.getOrderTotal()).append("</p>")
                .append("<p>Enjoy your movie!</p>");

        return content.toString();
    }


}



