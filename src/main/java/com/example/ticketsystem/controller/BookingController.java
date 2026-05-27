package com.example.ticketsystem.controller;

import com.example.ticketsystem.service.BookingService;
import com.example.ticketsystem.repository.EventRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final EventRepository eventRepository;

    // Dependency Injection via Constructor
    public BookingController(BookingService bookingService, EventRepository eventRepository) {
        this.bookingService = bookingService;
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody BookingRequest request) {
        Long userId = request.getUserId();
        Long eventId = request.getEventId();
        
        try {
            // Attempt to execute secure booking logic
            String result = bookingService.bookTicketSecure(userId, eventId);
            
            if ("SOLD_OUT".equals(result)) {
                return ResponseEntity.badRequest().body("Tickets are sold out!");
            }
            return ResponseEntity.ok("Booking successful!");
            
        } catch (Exception e) {
            // Print full error trace to your Eclipse console
            e.printStackTrace();
            
            // Expose the raw error description to the website UI
            return ResponseEntity.status(500)
                    .body("Server Crash Cause: " + e.getMessage());
        }
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<?> getEventStatus(@PathVariable Long id) {
        Integer tickets = eventRepository.getAvailableTicketsCount(id);
        
        if (tickets == null) {
            return ResponseEntity.ok(Map.of("availableTickets", 0));
        }
        
        return ResponseEntity.ok(Map.of("availableTickets", tickets));
    }
}

class BookingRequest {
    private Long userId;
    private Long eventId;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
}