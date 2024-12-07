package com.jts.movie.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jts.movie.entities.Ticket;
import com.jts.movie.enums.SeatStatus;
import com.jts.movie.repositories.TicketRepository;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    // Retrieve all tickets by booking ID
    public List<Ticket> getTicketsByBookingId(Integer bookingId) {
        List<Ticket> tickets = ticketRepository.findByBookingBookingId(bookingId);
        if (tickets.isEmpty()) {
            throw new RuntimeException("No tickets found for booking ID: " + bookingId);
        }
        return tickets;
    }

    // Cancel all tickets associated with a booking ID
    public void cancelTicketsByBookingId(Integer bookingId) {
        List<Ticket> tickets = ticketRepository.findByBookingBookingId(bookingId);
        if (tickets.isEmpty()) {
            throw new RuntimeException("No tickets found to cancel for booking ID: " + bookingId);
        }
        tickets.forEach(ticket -> ticket.setSeatStatus(SeatStatus.AVAILABLE)); // Set status to AVAILABLE or custom status
        ticketRepository.saveAll(tickets);
    }
}
