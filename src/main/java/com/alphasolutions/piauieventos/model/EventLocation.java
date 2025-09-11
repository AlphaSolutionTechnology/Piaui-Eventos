package com.alphasolutions.piauieventos.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name= "event_location")
@Data
public class EventLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_location")
    private Long id;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "full_address", length = 500)
    private String fullAddress;

    @Column(name = "place_name", length = 150)
    private String placeName;

    @Column(name = "zip_code", length = 9)
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private LocationCategory categoryId;

}
