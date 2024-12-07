package com.jts.movie.services;

import com.jts.movie.entities.Movie;
import com.jts.movie.entities.User;
import com.jts.movie.repositories.MovieRepository;
import com.jts.movie.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;


    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    // Method to update an existing movie
    public Movie updateMovie(Long id, Movie movieDetails) throws Exception {
        // Find the movie by ID
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new Exception("Movie not found with ID: " + id));

        // Update fields with new values
        existingMovie.setMovieName(movieDetails.getMovieName());
        existingMovie.setDuration(movieDetails.getDuration());
        existingMovie.setMpaaRating(movieDetails.getMpaaRating());
        existingMovie.setReleaseDate(movieDetails.getReleaseDate());
        existingMovie.setDescription(movieDetails.getDescription());
        existingMovie.setImageUrl(movieDetails.getImageUrl());
        existingMovie.setTrailerUrl(movieDetails.getTrailerUrl());
        existingMovie.setCast(movieDetails.getCast());
        existingMovie.setDirector(movieDetails.getDirector());
        existingMovie.setGenre(movieDetails.getGenre());
        existingMovie.setCategory(movieDetails.getCategory());
        existingMovie.setLanguage(movieDetails.getLanguage());

        // Save updated movie
        return movieRepository.save(existingMovie);
    }

    public void deleteMovie(Long movieId) throws Exception {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new Exception("Movie not found with ID: " + movieId));
        movieRepository.delete(movie);
    }

    public User activateUser(long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found with ID: " + userId));
        user.setIsActive(true);
        return userRepository.save(user);
    }

    public User suspendUser(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found with ID: " + userId));
        user.setIsActive(false);
        return userRepository.save(user);
    }
}

