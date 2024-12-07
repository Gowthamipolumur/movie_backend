package com.jts.movie.repositories;

import com.jts.movie.entities.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jts.movie.entities.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	Optional<Movie> findByMovieName(String name);
	//@Query("SELECT s FROM Show s WHERE s.movie.movieName = :title AND s.showDateTime = :showDateTime")
	//Optional<Movie> findByMovieNameAndShowDateTime(String movieName, LocalDateTime showDateTime);
}

