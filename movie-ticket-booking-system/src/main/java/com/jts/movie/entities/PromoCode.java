package com.jts.movie.entities;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import com.jts.movie.enums.genre;
import com.jts.movie.enums.language;

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
@Table(name = "PromoCode")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PromoCode {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String promocode_value;

    private Date ExpiryDate;
}


