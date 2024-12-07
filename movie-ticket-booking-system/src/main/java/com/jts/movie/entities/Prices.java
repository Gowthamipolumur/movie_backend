package com.jts.movie.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRICES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prices {

    @Id
    @Column(name = "id", nullable = false)
    private Long id = 1L;  // Set ID to a fixed value, only one row allowed

    @Column(nullable = false)
    private Double childPrice;

    @Column(nullable = false)
    private Double adultPrice;

    @Column(nullable = false)
    private Double seniorPrice;

    @Column(nullable = false)
    private Double onlineBookingFee;

    // We can initialize this record in the database when the application starts
}
