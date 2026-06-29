package com.pkshop.domain.catalog.repository;

import com.pkshop.domain.catalog.entity.CarBrand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {
    boolean existsByNameIgnoreCase(String name);
}