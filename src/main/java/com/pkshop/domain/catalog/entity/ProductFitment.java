package com.pkshop.domain.catalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="product_fitments")
@Data
public class ProductFitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="product_id")
    @JsonIgnore
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="car_brand_id")
    private CarBrand carBrand;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="car_model_id")
    private CarModel carModel;

    @Column(name="year_from", nullable=false)
    private Integer yearFrom;

    @Column(name="year_to")
    private Integer yearTo;

    @Column(length=255)
    private String notes;
}