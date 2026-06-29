package com.pkshop.domain.importflow.repository;

import com.pkshop.domain.importflow.entity.ImportLotItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportLotItemRepository extends JpaRepository<ImportLotItem, Long> {
    List<ImportLotItem> findByImportLotId(Long importLotId);

}
