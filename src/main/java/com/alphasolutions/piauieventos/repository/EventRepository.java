package com.alphasolutions.piauieventos.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.alphasolutions.piauieventos.model.Event;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @NotNull Page<Event> findAll(@NotNull Pageable pageable);
}
