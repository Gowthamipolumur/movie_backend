package com.jts.movie.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.jts.movie.enums.genre;
import com.jts.movie.enums.language;
import com.jts.movie.enums.category;

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

    @Column(precision = 3, scale = 2)  // Precision for 1 decimal (e.g., 8.5)
    private BigDecimal rating;

    private Date releaseDate;
    private String description;
    private String imageUrl;
    private String trailerUrl;
    private String cast;
    private String director;

    @Enumerated(value = EnumType.STRING)
    private genre genre;

    @Enumerated(value = EnumType.STRING)
    private category category;

    @Enumerated(value = EnumType.STRING)
    private language language;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Show> shows = new ArrayList<>();
}
