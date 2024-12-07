package com.jts.movie.convertor;

import com.jts.movie.entities.Ticket;
import com.jts.movie.enums.SeatNumber;
import com.jts.movie.enums.SeatStatus;
import com.jts.movie.request.TicketRequest;
import java.util.List;
import java.util.stream.Collectors;

public class TicketConvertor {

    // Corrected method to convert TicketRequest to Ticket entity
    public Ticket convertToEntity(TicketRequest ticketRequest) {
        Ticket ticket = new Ticket();
        ticket.setSeatNumber(SeatNumber.valueOf(ticketRequest.getSeatNumber().name().toUpperCase()));
        ticket.setCategory(ticketRequest.getCategory());
        ticket.setPrice(ticketRequest.getPrice()); // Map price from request
        ticket.setSeatStatus(SeatStatus.valueOf(ticketRequest.getSeatStatus().name().toUpperCase()));
        return ticket;
    }

    // Method to convert a list of TicketRequest to list of Ticket entities
    public List<Ticket> convertToEntities(List<TicketRequest> ticketRequests) {
        return ticketRequests.stream()
                .map(this::convertToEntity)  // Using the method reference
                .collect(Collectors.toList());
    }
}

