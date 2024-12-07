package com.jts.movie.request;

import lombok.Data;
import java.util.List;

@Data
public class BookingRequest {
    private Long userId;
    private String movieName;
    private Long showId;
    private List<TicketRequest> tickets; // Age category handled in each ticket
    private Integer numChildren;
    private Integer numAdults;
    private Integer numSeniors;
    private Double ticketTotal;
    private Double bookingFee;
    private Double tax;
    private Double orderTotal;
}
