package com.pkshop.domain.catalog.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="car_models",
        uniqueConstraints = @UniqueConstraint(name="uq_brand_model", columnNames={"brand_id","name"}))
@Data
public class CarModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="brand_id")
    private CarBrand brand;

    @Column(nullable=false, length=120)
    private String name;
}