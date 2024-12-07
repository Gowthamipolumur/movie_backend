package com.jts.movie.controller;

import com.jts.movie.request.BookingRequest;
import com.jts.movie.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createBooking(@RequestBody BookingRequest request) {
        Map<String, Object> bookingSummary = bookingService.createBooking(request);
        return ResponseEntity.ok(bookingSummary);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getBookingsByUserId(@PathVariable Long userId) {
        List<Map<String, Object>> response = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
