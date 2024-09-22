package com.jts.movie.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;
import com.jts.movie.request.MovieRequest;
import com.jts.movie.services.MovieService;
import com.jts.movie.entities.Movie;
@RestController
@RequestMapping("/movie")
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://example.com"})
public class MovieController {
	
	@Autowired
    private MovieService movieService;

    @GetMapping("/test")
    public String returntest(){
        return "Test";
    }

    @PostMapping("/addNew")
    public ResponseEntity<String> addMovie(@RequestBody MovieRequest movieRequest) {
        try {
            log.info("Request received for adding movie: {}", movieRequest.getMovieName());
            String result = movieService.addMovie(movieRequest);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error while adding movie", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // New endpoint to get all movies
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

}