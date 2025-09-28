package com.alphasolutions.piauieventos.repository;

import com.alphasolutions.piauieventos.model.Subscription;
import com.alphasolutions.piauieventos.model.SubscriptionId;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
    long countByEventId(Long eventId);

    @Query("SELECT s.id.eventId FROM Subscription s GROUP BY s.id.eventId ORDER BY COUNT(s) DESC")
    List<Long> countTopSubscriptionsByEvent(Limit limit);
}
