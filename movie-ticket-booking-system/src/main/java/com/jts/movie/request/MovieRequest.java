package com.jts.movie.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.sql.Date;
import com.jts.movie.enums.genre;
import com.jts.movie.enums.language;
import com.jts.movie.enums.category;
import com.jts.movie.enums.MPAAUSRating;

@Data  // This generates getters, setters, toString, equals, and hashcode methods
@AllArgsConstructor
@ToString
public class MovieRequest {
	private String movieName;
	private Integer duration;
	private Double rating;
	private Date releaseDate;
	private genre genre;
	private MPAAUSRating mpaaRating;
	private language language;
	private String description;
	private String imageUrl;
	private String trailerUrl;
	private String cast;
	private String director;
	private category category;
}
