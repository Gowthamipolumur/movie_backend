package com.jts.movie.services;

import com.jts.movie.request.BookingRequest;
import com.jts.movie.entities.*;
import com.jts.movie.enums.SeatNumber;
import com.jts.movie.enums.SeatStatus;
import com.jts.movie.repositories.*;
import com.jts.movie.request.TicketRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");


    @Transactional
    public Map<String, Object> createBooking(BookingRequest request) {

        // Fetch User and Show entities
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with ID: " + request.getShowId()));

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
