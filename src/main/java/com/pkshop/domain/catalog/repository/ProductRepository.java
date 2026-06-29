package com.pkshop.domain.catalog.repository;

import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.dto.admin.catalog.ProductOptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByIsActiveTrue(Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCaseAndIsActiveTrue(
            String name,
            String sku,
            Pageable pageable
    );

    Optional<Product> findByIdAndIsActiveTrue(Long id);

    Page<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(
            String name,
            String sku,
            Pageable pageable
    );

    @Query("""
        select new com.pkshop.dto.admin.catalog.ProductOptionResponse(
            p.id, p.sku, p.name, p.importCostAvg, p.stockQty
        )
        from Product p
        where p.stockQty = 0
          and (:q = '' or lower(p.name) like lower(concat('%', :q, '%'))
                     or lower(p.sku)  like lower(concat('%', :q, '%')))
        order by p.id desc
    """)
    List<ProductOptionResponse> findOptionsForPo(@Param("q") String q);

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.fitments f " +
            "LEFT JOIN FETCH f.carModel m " +
            "LEFT JOIN FETCH m.brand " +
            "WHERE p.id = :id")
    Optional<Product> findByIdWithDetails(@Param("id") Long id);
}