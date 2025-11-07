package com.alphasolutions.piauieventos.repository;

import com.alphasolutions.piauieventos.model.Subscription;
import com.alphasolutions.piauieventos.model.SubscriptionId;
import com.alphasolutions.piauieventos.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {

    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.event.id = :eventId")
    Long countByEventId(@Param("eventId") Long eventId);

    List<Subscription> findAllByUser(UserModel user);
}
