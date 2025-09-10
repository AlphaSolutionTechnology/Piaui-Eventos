package com.alphasolutions.piauieventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alphasolutions.piauieventos.model.EventLocation;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLocationRepository extends JpaRepository<EventLocation, Long> {

}
