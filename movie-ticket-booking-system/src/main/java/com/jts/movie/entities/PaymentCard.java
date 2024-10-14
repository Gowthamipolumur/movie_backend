package com.jts.movie.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PAYMENT_CARDS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String cardHolderName;

    @Column(nullable = false)
    private String expiryDate;

    @Column(nullable = false)
    private String cvv;

    // Many cards can be linked to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
