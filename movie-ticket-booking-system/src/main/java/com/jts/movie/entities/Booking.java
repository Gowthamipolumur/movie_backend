package com.jts.movie.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "BOOKINGS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "show", "tickets"})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime bookingDateTime; // Combines bookingDate and bookingTime

    @Column(nullable = false)
    private String movieName;

    private String promoName;

    private Double totalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", referencedColumnName = "show_id", nullable = false)
    private Show show;

    private Integer numChildren;
    private Integer numAdults;
    private Integer numSeniors;

    @Column(nullable = false)
    private Double ticketTotal;

    @Column(nullable = false)
    private Double bookingFee;

    @Column(nullable = false)
    private Double tax;

    @Column(nullable = false)
    private Double orderTotal;



    @Column(nullable = false, unique = true) // Ensures uniqueness
    private String bookingNumber;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

}
