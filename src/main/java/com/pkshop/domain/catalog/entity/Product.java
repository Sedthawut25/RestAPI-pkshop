package com.pkshop.domain.catalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "products")
@Data
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String sku;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @OneToMany(
            mappedBy = "product",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<ProductFitment> fitments = new HashSet<>();


    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name="import_cost_avg", nullable = false)
    private BigDecimal importCostAvg;

    @Column(name="stock_qty", nullable = false)
    private Integer stockQty;

    @Column(name="is_active", nullable = false)
    private Boolean isActive = true;

    private Integer stock;
}
