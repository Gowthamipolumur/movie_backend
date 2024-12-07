package com.jts.movie.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jts.movie.enums.genre;
import com.jts.movie.enums.language;
import com.jts.movie.enums.category;
import com.jts.movie.enums.MPAAUSRating;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties({"shows"})
@Entity
@Table(name = "MOVIES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String movieName;

    private Integer duration;

    @Enumerated(value = EnumType.STRING)
    private MPAAUSRating mpaaRating;

    private Date releaseDate;
    private String description;
    private String imageUrl;
    private String trailerUrl;
    private String cast;
    private String director;
    private String producers;

    @Enumerated(value = EnumType.STRING)
    private genre genre;

    @Enumerated(value = EnumType.STRING)
    private category category;

    @Enumerated(value = EnumType.STRING)
    private language language;

//    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
//    private List<Show> shows = new ArrayList<>();
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Show> shows = new ArrayList<>();

}
