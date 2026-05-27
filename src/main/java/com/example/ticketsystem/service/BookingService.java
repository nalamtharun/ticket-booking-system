package com.example.ticketsystem.service;

import com.example.ticketsystem.entity.Booking;
import com.example.ticketsystem.entity.Event;
import com.example.ticketsystem.repository.BookingRepository;
import com.example.ticketsystem.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class BookingService {

    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;

    // Dependency Injection via Constructor
    public BookingService(EventRepository eventRepository, BookingRepository bookingRepository) {
        this.eventRepository = eventRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * SECURE BOOKING LOGIC using Database Pessimistic Locking
     * This eliminates race conditions during heavy concurrent JMeter loads.
     */
    @Transactional
    public String bookTicketSecure(Long userId, Long eventId) {
        // 1. Fetch Event and acquire an exclusive PESSIMISTIC_WRITE lock on this row
        Event event = eventRepository.findByIdWithLock(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // 2. Check availability safely (no other thread can alter this while we check)
        if (event.getAvailableTickets() <= 0) {
            return "SOLD_OUT";
        }

        // 3. Decrement Inventory safely
        event.setAvailableTickets(event.getAvailableTickets() - 1);
        eventRepository.save(event); 

        // 4. Create and populate a new Booking object
        Booking booking = new Booking();
        booking.setId(UUID.randomUUID().toString()); 
        booking.setUserId(userId);
        booking.setEventId(eventId);
        booking.setBookingStatus("CONFIRMED");
        
        // 5. Save Booking to MySQL
        bookingRepository.save(booking);

        return "SUCCESS";
    }
}