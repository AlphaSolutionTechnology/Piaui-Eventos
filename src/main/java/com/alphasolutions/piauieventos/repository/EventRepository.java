package com.alphasolutions.piauieventos.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.alphasolutions.piauieventos.model.Event;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByEventDateGreaterThanEqualOrderByEventDateDesc(LocalDateTime date, Limit limit);
    List<Event> findAllByOrderByEventDateDesc(Pageable pageable);
    List<Event> findByEventDateBetweenOrderByEventDateAsc(LocalDateTime start, LocalDateTime end, Pageable pageable);

}