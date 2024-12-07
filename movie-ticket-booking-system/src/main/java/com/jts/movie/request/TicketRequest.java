package com.jts.movie.request;

import com.jts.movie.enums.SeatNumber;
import com.jts.movie.enums.SeatStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TicketRequest {
    private SeatNumber seatNumber; // Enum for seat numbers (e.g., A1, B2, etc.)
    private String category;       // e.g., "Adult", "Child", "Senior"
    private Double price;          // Ticket price
    private SeatStatus seatStatus;
}
