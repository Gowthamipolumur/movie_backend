package com.jts.movie.services;

import com.jts.movie.request.BookingRequest;
import com.jts.movie.entities.*;
import com.jts.movie.enums.SeatNumber;
import com.jts.movie.enums.SeatStatus;
import com.jts.movie.repositories.*;
import com.jts.movie.request.TicketRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import static java.lang.Boolean.TRUE;

@Service
public class BookingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserPromoRepository userPromoRepository; // Repository for UserPromo entity

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Transactional
    public Map<String, Object> createBooking(BookingRequest request) {

        // Fetch User and Show entities
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with ID: " + request.getShowId()));

        // Validate Promo Code if provided
        if (request.getPromoName() != null && !request.getPromoName().isEmpty()) {
            // Fetch UserPromo for the given promo code and user email
            UserPromo userPromo = userPromoRepository.findByUserTokenAndPromoName(request.getPromoName(), user.getEmailId())
                    .orElseThrow(() -> new RuntimeException("Promo code not found or invalid for user: " + request.getPromoName()));

            // Check if the promo code has already been used
            if (Boolean.TRUE.equals(userPromo.getIsUsed())) {
                throw new RuntimeException("Promo code has already been used.");
            }

            // Set the promo code as used after successful booking
            userPromo.setIsUsed(TRUE);
            userPromoRepository.save(userPromo);
        }

        // Validate and process tickets
        for (TicketRequest ticket : request.getTickets()) {
            if (!SeatNumber.isValidSeat(ticket.getSeatNumber().name())) {
                throw new RuntimeException("Invalid seat number: " + ticket.getSeatNumber());
            }

            if (show.getBookedSeats().contains(ticket.getSeatNumber().name())) {
                throw new RuntimeException("Seat " + ticket.getSeatNumber() + " is already booked.");
            }
        }

        // Update the show's booked seats
        show.getBookedSeats().addAll(request.getTickets().stream()
                .map(ticket -> ticket.getSeatNumber().name())
                .collect(Collectors.toList()));
        showRepository.save(show);

        // Create and save Booking
        Booking booking = Booking.builder()
                .user(user)
                .movieName(request.getMovieName())
                .show(show)
                .bookingDateTime(LocalDateTime.now())
                .numChildren(request.getNumChildren())
                .numAdults(request.getNumAdults())
                .numSeniors(request.getNumSeniors())
                .ticketTotal(request.getTicketTotal())
                .bookingFee(request.getBookingFee())
                .tax(request.getTax())
                .orderTotal(request.getOrderTotal())
                .bookingNumber("BKG-" + System.currentTimeMillis())
                .build();

        booking = bookingRepository.save(booking);

        // Initialize tickets list if null
        if (booking.getTickets() == null) {
            booking.setTickets(new ArrayList<>());
        }

        // Create and save Ticket entities
        for (TicketRequest ticketRequest : request.getTickets()) {
            Ticket ticket = Ticket.builder()
                    .booking(booking)
                    .seatNumber(SeatNumber.valueOf(ticketRequest.getSeatNumber().name()))
                    .seatStatus(SeatStatus.BOOKED)
                    .category(ticketRequest.getCategory())
                    .price(ticketRequest.getPrice())
                    .bookingNumber(booking.getBookingNumber())
                    .build();
            booking.getTickets().add(ticket);
            ticketRepository.save(ticket);
        }
        // Send the booking confirmation email
        try {
            sendBookingConfirmationEmail(user, booking, show);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending booking confirmation email: " + e.getMessage());
        }

        // Construct the API response
        Map<String, Object> response = new HashMap<>();
        response.put("bookingId", booking.getBookingId());
        response.put("userName", user.getFirstName() + " " + user.getLastName());
        response.put("movieName", booking.getMovieName());
        response.put("showDate", show.getDate().toString());
        response.put("showTime", show.getTime().format(TIME_FORMATTER));
        response.put("bookingDate", booking.getBookingDateTime());
        response.put("theaterName", show.getTheater().getName());
        response.put("tickets", booking.getTickets().stream()
                .map(ticket -> Map.of(
                        "seatNumber", ticket.getSeatNumber().name(),
                        "category", ticket.getCategory()))
                .collect(Collectors.toList()));
        response.put("totalCost", booking.getOrderTotal());
        response.put("ticketTotal", booking.getTicketTotal());
        response.put("tax", booking.getTax());
        response.put("bookingFee", booking.getBookingFee());

        return response;
    }

    private void sendBookingConfirmationEmail(User user, Booking booking, Show show) throws MessagingException {
        // Prepare email content
        String subject = "Booking Confirmation - " + booking.getMovieName();
        String body = "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>"
                + "<p>Thank you for your booking! Here are your booking details:</p>"
                + "<p><strong>Movie:</strong> " + booking.getMovieName() + "</p>"
                + "<p><strong>Theater:</strong> " + show.getTheater().getName() + "</p>"
                + "<p><strong>Show Date:</strong> " + show.getDate() + "</p>"
                + "<p><strong>Show Time:</strong> " + show.getTime().format(TIME_FORMATTER) + "</p>"
                + "<p><strong>Booking Date:</strong> " + booking.getBookingDateTime() + "</p>"
                + "<p><strong>Total Cost:</strong> " + booking.getOrderTotal() + "</p>"
                + "<p><strong>Tickets:</strong></p><ul>";

        for (Ticket ticket : booking.getTickets()) {
            body += "<li>Seat: " + ticket.getSeatNumber().name() + " - Category: " + ticket.getCategory() + " - Price: " + ticket.getPrice() + "</li>";
        }

        body += "</ul><p>We hope you enjoy your movie experience!</p>";

        // Send email
        sendEmail(user.getEmailId(), subject, body);
    }

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // true means HTML email
        mailSender.send(mimeMessage);
    }

    public List<Map<String, Object>> getBookingsByUserId(Long userId) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Fetch bookings for the user
        List<Booking> bookings = bookingRepository.findByUserId(userId);

        // Transform bookings into a list of maps (or DTOs)
        return bookings.stream().map(booking -> {
            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put("bookingId", booking.getBookingId());
            bookingData.put("bookingNumber", booking.getBookingNumber());
            bookingData.put("movieName", booking.getMovieName());
            bookingData.put("showDate", booking.getShow().getDate());
            bookingData.put("showTime", booking.getShow().getTime());
            bookingData.put("theaterName", booking.getShow().getTheater().getName());
            bookingData.put("tickets", booking.getTickets().stream()
                    .map(ticket -> Map.of(
                            "seatNumber", ticket.getSeatNumber().name(),
                            "category", ticket.getCategory()
                    ))
                    .collect(Collectors.toList()));
            bookingData.put("totalCost", booking.getOrderTotal());
            return bookingData;
        }).collect(Collectors.toList());
    }
}
