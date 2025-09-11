package com.alphasolutions.piauieventos.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "location_category")
@Data
public class LocationCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "category_name", length = 100)
    private String categoryName;
}

