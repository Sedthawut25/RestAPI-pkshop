package com.pkshop.domain.catalog.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table (name = "categories")
@Data
public class Category {
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true , length = 120)
    private String name;
}
