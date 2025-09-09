package com.alphasolutions.piauieventos.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name= "events")
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event")
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "max_subs")
    private Integer maxSubs;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_location", nullable = false)
    private EventLocation location;
}
