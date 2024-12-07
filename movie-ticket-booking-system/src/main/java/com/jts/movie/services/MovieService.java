package com.jts.movie.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

import com.jts.movie.convertor.MovieConvertor;
import com.jts.movie.entities.Movie;
import com.jts.movie.exceptions.MovieAlreadyExist;
import com.jts.movie.repositories.MovieRepository;
import com.jts.movie.request.MovieRequest;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;

	// Method to add a movie
	public String addMovie(MovieRequest movieRequest) throws Exception {
		// Check if a movie with the same name and language already exists
		Optional<Movie> movieByName = movieRepository.findByMovieName(movieRequest.getMovieName());

		if (movieByName.isPresent() &&
				movieByName.get().getLanguage().equals(movieRequest.getLanguage())) {
			throw new MovieAlreadyExist(); // Custom exception for movie already existing
		}

		// Convert the MovieRequest DTO to a Movie entity
		Movie movie = MovieConvertor.movieDtoToMovie(movieRequest);

		// Save the movie to the database
		movieRepository.save(movie);

		return "The movie has been added successfully";
	}

	// Method to get all movies
	public List<Movie> getAllMovies() {
		return movieRepository.findAll();
	}

	// Method to fetch a movie by ID
	public Movie getMovieById(Long id) throws Exception {
		// Find the movie by ID or throw an exception if not found
		return movieRepository.findById(id)
				.orElseThrow(() -> new Exception("Movie not found with ID: " + id));
	}
}
