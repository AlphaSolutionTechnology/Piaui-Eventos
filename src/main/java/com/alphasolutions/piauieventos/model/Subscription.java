package com.alphasolutions.piauieventos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscriptions")
@Data
@NoArgsConstructor
public class Subscription {

    @EmbeddedId
    private SubscriptionId id;

    // Relacionamento com a entidade User.
    // '@ManyToOne': Muitas inscrições para um usuário.
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "id_user")
    private UserModel user;

    // Relacionamento com a entidade Event.
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("eventId")
    @JoinColumn(name = "id_event")
    private Event event;
}
