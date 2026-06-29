package com.pkshop.domain.catalog.repository;

import com.pkshop.domain.catalog.entity.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {

    List<CarModel> findByBrand_Id(Long brandId);
}