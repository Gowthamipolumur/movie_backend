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
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000","http://localhost:51793"})
public class MovieController {
	
	@Autowired
    private MovieService movieService;

    @GetMapping("/test")
    public String returntest(){
        return "Test";
    }


    // New endpoint to get all movies
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    // New endpoint to get all movies by id
    // Endpoint to get movie by ID
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable("id") Long id) throws Exception {
        Movie movie = movieService.getMovieById(id);
        if (movie != null) {
            return new ResponseEntity<>(movie, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if the movie is not found
        }
    }
}