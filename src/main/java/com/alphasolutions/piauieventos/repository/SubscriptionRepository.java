package com.alphasolutions.piauieventos.repository;

import com.alphasolutions.piauieventos.model.Subscription;
import com.alphasolutions.piauieventos.model.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
    List<Subscription> findByEventId(Long eventId);
}
