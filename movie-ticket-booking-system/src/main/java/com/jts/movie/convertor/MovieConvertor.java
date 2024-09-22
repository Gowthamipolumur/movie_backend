package com.jts.movie.convertor;

import com.jts.movie.entities.Movie;
import com.jts.movie.request.MovieRequest;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.sql.Date;

public class MovieConvertor {

    public static Movie movieDtoToMovie(MovieRequest movieRequest) {
        Movie movie = Movie.builder()
                .movieName(movieRequest.getMovieName())
                .duration(movieRequest.getDuration())
                .genre(movieRequest.getGenre())
                .language(movieRequest.getLanguage())
                .releaseDate(movieRequest.getReleaseDate())
                .rating(BigDecimal.valueOf(movieRequest.getRating()))
                .description(movieRequest.getDescription())
                .imageUrl(movieRequest.getImageUrl())
                .trailerUrl(movieRequest.getTrailerUrl())
                .cast(movieRequest.getCast())
                .director(movieRequest.getDirector())
                .category(movieRequest.getCategory())
                .build();
        return movie;
    }
}