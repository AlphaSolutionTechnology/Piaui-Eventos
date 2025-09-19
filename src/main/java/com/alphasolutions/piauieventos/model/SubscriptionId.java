package com.alphasolutions.piauieventos.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionId implements Serializable {

    private Long userId;
    private Long eventId;
}