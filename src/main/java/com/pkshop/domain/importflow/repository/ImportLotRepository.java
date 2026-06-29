package com.pkshop.domain.importflow.repository;

import com.pkshop.domain.importflow.entity.ImportLot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImportLotRepository extends JpaRepository<ImportLot, Long> {
    Optional<ImportLot> findByLotNumber(String lotNumber);
}
