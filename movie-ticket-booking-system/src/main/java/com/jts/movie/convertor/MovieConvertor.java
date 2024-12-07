package com.jts.movie.convertor;

import com.jts.movie.entities.Movie;
import com.jts.movie.enums.MPAAUSRating;
import com.jts.movie.request.MovieRequest;

public class MovieConvertor {

    public static Movie movieDtoToMovie(MovieRequest movieRequest) {
        Movie movie = Movie.builder()
                .movieName(movieRequest.getMovieName())
                .duration(movieRequest.getDuration())
                .genre(movieRequest.getGenre())
                .language(movieRequest.getLanguage())
                .releaseDate(movieRequest.getReleaseDate())
                .mpaaRating(movieRequest.getMpaaRating()) // Corrected line
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
