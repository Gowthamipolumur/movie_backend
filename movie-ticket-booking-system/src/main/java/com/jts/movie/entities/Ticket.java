package com.jts.movie.entities;

import com.jts.movie.enums.SeatNumber;
import com.jts.movie.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TICKETS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticket_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "booking_id", nullable = false)
	private Booking booking;

	@Enumerated(EnumType.STRING)
	@Column(name = "seat_number", nullable = false)
	private SeatNumber seatNumber;

	@Column(name = "category", nullable = false)
	private String category; // Adult, Child, Senior

	@Column(name = "price")
	private Double price;

	@Enumerated(EnumType.STRING)
	@Column(name = "seat_status", nullable = false)
	private SeatStatus seatStatus; // e.g., "Booked", "Cancelled"

	@Column(name = "booking_number", nullable = false)
	private String bookingNumber; // To associate ticket with the booking
}
