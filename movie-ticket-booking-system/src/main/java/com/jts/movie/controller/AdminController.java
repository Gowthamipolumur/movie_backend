package com.jts.movie.controller;

import com.jts.movie.entities.Promotion;
import com.jts.movie.entities.User;
import com.jts.movie.services.PromotionService;
import com.jts.movie.entities.Movie;
import com.jts.movie.entities.Show;
import com.jts.movie.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jts.movie.services.ShowService;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ShowService showService;

    @Autowired
    private PromotionService promotionService;

    @PostMapping("/addMovie")
    public ResponseEntity<Map<String, Object>> addMovie(@RequestBody Movie movie) {
        Map<String, Object> response = new HashMap<>();
        try {
            Movie savedMovie = adminService.addMovie(movie);
            response.put("message", "Movie added successfully.");
            response.put("movie", savedMovie);
            response.put("statusCode", HttpStatus.CREATED.value());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/editMovies/{id}")
    public ResponseEntity<Map<String, Object>> editMovie(@PathVariable Long id, @RequestBody Movie movie) {
        Map<String, Object> response = new HashMap<>();
        try {
            Movie updatedMovie = adminService.updateMovie(id, movie);
            response.put("message", "Movie updated successfully.");
            response.put("movie", updatedMovie);
            response.put("statusCode", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/movies/{movieId}")
    public ResponseEntity<Map<String, Object>> deleteMovie(@PathVariable Long movieId) {
        Map<String, Object> response = new HashMap<>();
        try {
            adminService.deleteMovie(movieId);
            response.put("message", "Movie deleted successfully.");
            response.put("statusCode", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error deleting movie: " + e.getMessage());
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/scheduleMovies")
    public ResponseEntity<Map<String, Object>> scheduleShow(
            @RequestParam Long movieId,
            @RequestParam String date, // Using String to parse to LocalDate
            @RequestParam String time, // Using String to parse to LocalTime
            @RequestParam Long theaterId) {

        Map<String, Object> response = new HashMap<>();
        try {
            // Validate input parameters
            if (movieId == null || theaterId == null || date == null || time == null) {
                response.put("message", "Missing required parameters.");
                response.put("statusCode", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Parse date and time parameters
            java.sql.Date showDate;
            java.sql.Time showTime;
            try {
                showDate = java.sql.Date.valueOf(date); // Convert String to java.sql.Date
                showTime = java.sql.Time.valueOf(time); // Convert String to java.sql.Time
            } catch (IllegalArgumentException ex) {
                response.put("message", "Invalid date or time format. Use 'yyyy-MM-dd' for date and 'HH:mm:ss' for time.");
                response.put("statusCode", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Check for scheduling conflicts
            boolean isConflict = showService.checkScheduleConflict(theaterId, showDate, showTime);
            if (isConflict) {
                response.put("message", "A show is already scheduled at the same time, date, and theater.");
                response.put("statusCode", HttpStatus.CONFLICT.value());
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
            // Schedule the show
            Show scheduledShow = showService.scheduleShow(movieId, theaterId, showDate, showTime);

            response.put("message", "Show scheduled successfully.");
            response.put("showId", scheduledShow.getId());
            response.put("statusCode", HttpStatus.CREATED.value());
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace();

            response.put("message", "Error scheduling show: " + e.getMessage());
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // Add Promotion Endpoint
    @PostMapping("/AddPromo")
    public ResponseEntity<Map<String, Object>> addPromotion(@RequestBody Promotion promotion) {
        Map<String, Object> response = new HashMap<>();
        promotion.updateValidity();
        Promotion savedPromotion = promotionService.addPromotion(promotion);
        response.put("message", "Promotion added successfully.");
        response.put("promotion", savedPromotion);
        response.put("statusCode", HttpStatus.CREATED.value());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get All Promotions Endpoint
    @GetMapping("/promotions")
    public ResponseEntity<Map<String, Object>> getAllPromotions() {
        Map<String, Object> response = new HashMap<>();
        List<Promotion> promotions = promotionService.getAllPromotions();
        response.put("promotions", promotions);
        response.put("statusCode", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/sendPromo/{id}")
//    public ResponseEntity<String> sendPromotion(@PathVariable Long id) {
//        try {
//            promotionService.sendPromotion(id);
//            return ResponseEntity.ok("Promotion sent successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

    @PostMapping("/sendPromo/{id}")
    public ResponseEntity<Map<String, Object>> sendPromotion(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            promotionService.sendPromotion(id);
            response.put("success", true);
            response.put("message", "Promotion sent successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error sending promotion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    // New endpoint to delete a promotion by ID
    @DeleteMapping("/promotions/{promoId}")
    public ResponseEntity<Map<String, Object>> deletePromotion(@PathVariable Long promoId) {
        Map<String, Object> response = new HashMap<>();
        try {
            promotionService.deletePromotion(promoId);
            response.put("message", "Promotion deleted successfully.");
            response.put("statusCode", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error deleting promotion: " + e.getMessage());
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/promo/{id}")
    public ResponseEntity<Map<String, Object>> getPromotionById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Promotion> promotionOptional = promotionService.getPromotionById(id);
            if (promotionOptional.isPresent()) {
                Promotion promotion = promotionOptional.get();
                response.put("statusCode", HttpStatus.OK.value());
                response.put("message", "Promotion found.");
                response.put("promotion", promotion);
                return ResponseEntity.ok(response);
            } else {
                response.put("statusCode", HttpStatus.NOT_FOUND.value());
                response.put("message", "Promotion not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @PutMapping("/ActivateUser/{userId}")
    public ResponseEntity<Map<String, Object>> activateUser(@PathVariable Integer userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            User updatedUser = adminService.activateUser(userId);
            response.put("message", "User activated successfully.");
            response.put("user", updatedUser);
            response.put("statusCode", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/SuspendUser/{userId}")
    public ResponseEntity<Map<String, Object>> suspendUser(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            User updatedUser = adminService.suspendUser(userId);
            response.put("message", "User suspended successfully.");
            response.put("user", updatedUser);
            response.put("statusCode", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
