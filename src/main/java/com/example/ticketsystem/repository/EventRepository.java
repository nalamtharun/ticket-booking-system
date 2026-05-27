package com.example.ticketsystem.repository;

import com.example.ticketsystem.entity.Event;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Keeps your high-concurrency protection active for buying tickets
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Event e WHERE e.id = :id")
    Optional<Event> findByIdWithLock(@Param("id") Long id);

    // NEW LIGHTWEIGHT QUERY: Safely grabs just the remaining number for the web dashboard
    @Query(value = "SELECT available_tickets FROM events WHERE id = :id", nativeQuery = true)
    Integer getAvailableTicketsCount(@Param("id") Long id);
}