package com.jts.movie.request;

import lombok.Data;
import java.util.List;

@Data
public class BookingRequest {
    private Long userId;
    private String movieName;
    private Long showId;
    private String promoName;
    private List<TicketRequest> tickets;
    private int numChildren;
    private int numAdults;
    private int numSeniors;
    private double ticketTotal;
    private double bookingFee;
    private double tax;
    private double orderTotal;
}
