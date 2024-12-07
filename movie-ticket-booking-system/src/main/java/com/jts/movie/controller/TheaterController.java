package com.jts.movie.controller;

import com.jts.movie.entities.Theater;
import com.jts.movie.services.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    // Endpoint to add a new theater
    @PostMapping
    public ResponseEntity<Map<String, Object>> addTheater(@RequestBody Theater theater) {
        Map<String, Object> response = new HashMap<>();
        try {
            Theater savedTheater = theaterService.addTheater(theater);
            response.put("message", "Theater added successfully.");
            response.put("theater", savedTheater);
            response.put("statusCode", HttpStatus.CREATED.value());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to get all theaters
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllTheaters() {
        Map<String, Object> response = new HashMap<>();
        List<Theater> theaters = theaterService.getAllTheaters();
        response.put("theaters", theaters);
        response.put("statusCode", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    // Endpoint to get a single theater by ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTheaterById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Theater theater = theaterService.getTheaterById(id);
            response.put("message", "Theater found.");
            response.put("theater", theater);
            response.put("statusCode", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Theater not found: " + e.getMessage());
            response.put("statusCode", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to update theater details
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTheater(@PathVariable Long id, @RequestBody Theater updatedTheater) {
        Map<String, Object> response = new HashMap<>();
        try {
            Theater theater = theaterService.updateTheater(id, updatedTheater);
            response.put("message", "Theater updated successfully.");
            response.put("theater", theater);
            response.put("statusCode", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error updating theater: " + e.getMessage());
            response.put("statusCode", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to delete a theater

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTheater(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            theaterService.deleteTheater(id);
            response.put("message", "Theater deleted successfully.");
            response.put("statusCode", HttpStatus.NO_CONTENT.value());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (Exception e) {
            response.put("message", "Error: Theater not found: " + e.getMessage());
            response.put("statusCode", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
