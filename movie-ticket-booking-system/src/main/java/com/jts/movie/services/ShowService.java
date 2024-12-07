package com.jts.movie.services;

import com.jts.movie.entities.Movie;
import com.jts.movie.entities.Show;
import com.jts.movie.entities.Theater;
import com.jts.movie.enums.SeatNumber;
import com.jts.movie.enums.SeatStatus;
import com.jts.movie.repositories.MovieRepository;
import com.jts.movie.repositories.TheaterRepository;
import com.jts.movie.repositories.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShowService {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private ShowRepository showRepository;

	@Autowired
	private TheaterRepository theaterRepository;

	public List<Show> getShowsByMovieId(Integer movieId) {
		return showRepository.findByMovieId(movieId);
	}

	public boolean checkScheduleConflict(Long theaterId, java.sql.Date showDate, java.sql.Time showTime) {
		return showRepository.existsByDateAndTimeAndTheaterId(showDate, showTime, theaterId);
	}

	public List<Show> getAvailableShows(Date selectedDate, Integer movieId) {
		return showRepository.findByMovieIdAndDate(movieId, selectedDate);
	}

	public Show scheduleShow(long movieId, Long theaterId, java.sql.Date Date, java.sql.Time Time) throws Exception {
		// Fetch the Movie entity
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> new Exception("Movie not found with ID: " + movieId));

		// Fetch the Theater entity
		Theater theater = theaterRepository.findById(theaterId)
				.orElseThrow(() -> new Exception("Theater not found with ID: " + theaterId));

		// Convert java.sql.Date and java.sql.Time to LocalDate and LocalTime
		LocalDate localDate = Date.toLocalDate();
		LocalTime localTime = Time.toLocalTime();

		// Create a new Show entity
		Show show = new Show();
		show.setMovie(movie);
		show.setTheater(theater);
		show.setDate(localDate);
		show.setTime(localTime);


		// Save and return the Show entity
		return showRepository.save(show);
	}
	// Method to fetch a Show by ID
	public Show getShowById(long id) throws Exception {
		return showRepository.findById(id)
				.orElseThrow(() -> new Exception("Show not found with ID: " + id));
	}


	public Map<String, Object> getBookedSeatsByShowId(Long showId) {
		Show show = showRepository.findById(showId)
				.orElseThrow(() -> new RuntimeException("Show not found with ID: " + showId));

		Map<String, Object> response = new HashMap<>();
		response.put("showId", show.getId());
		response.put("bookedSeats", show.getBookedSeats());
		return response;
	}


}
