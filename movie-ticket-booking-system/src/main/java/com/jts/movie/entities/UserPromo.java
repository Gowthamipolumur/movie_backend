package com.jts.movie.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER_PROMO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPromo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userToken;

    @ManyToOne
    @JoinColumn(name = "promo_id", nullable = false)
    private Promotion promo;

    @Column(nullable = false)
    private Boolean isUsed;

    @Column(name = "promo_name")
    private String promoName;
}
