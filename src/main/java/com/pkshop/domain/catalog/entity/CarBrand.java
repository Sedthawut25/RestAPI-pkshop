package com.pkshop.domain.catalog.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "car_brands")
@Data
public class CarBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String name;
}
