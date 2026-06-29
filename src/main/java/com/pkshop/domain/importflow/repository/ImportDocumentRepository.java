package com.pkshop.domain.importflow.repository;

import com.pkshop.domain.importflow.entity.ImportDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImportDocumentRepository extends JpaRepository<ImportDocument, Long> {
    List<ImportDocument> findByStatus(String status);
    List<ImportDocument> findByImportLotId(Long importLotId);

    @Query("""
        select d
        from ImportDocument d
        join fetch d.importLot l
        join fetch d.submittedBy s
        where d.id = :id
    """)
    Optional<ImportDocument> findByIdWithLotAndSubmitter(@Param("id") Long id);
}
