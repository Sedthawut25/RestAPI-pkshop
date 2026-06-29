package com.pkshop.domain.catalog.repository;

import com.pkshop.domain.catalog.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductQueryRepository {
    private final EntityManager em;

    public ProductQueryRepository(EntityManager em) {
        this.em = em;
    }

    public Page<Product> search (
            String keyword,
            Long categoryId,
            Long brandId,
            Long modelId,
            Integer year,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String sort,
            int page,
            int size
    ){
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> p = cq.from(Product.class);

        Join<Object, Object> fit = null;
        Join<Object, Object> model = null;
        Join<Object, Object> brand = null;

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isTrue(p.get("isActive")));

        if(keyword!=null && !keyword.isBlank()){
            String like = "%" + keyword.trim().toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(p.get("name")), like),
                    cb.like(cb.lower(p.get("sku")), like)
            ));
        }
        if(categoryId!=null){
            predicates.add(cb.equal(p.get("category").get("id"), categoryId));
        }
        if (minPrice != null) predicates.add(cb.greaterThanOrEqualTo(p.get("price"), minPrice));
        if (maxPrice != null) predicates.add(cb.lessThanOrEqualTo(p.get("price"), maxPrice));

        if(brandId!=null || modelId!=null || year!=null){
            fit = p.join("fitments", JoinType.LEFT);
            model = fit.join("carModel", JoinType.LEFT);
            brand = model.join("brand", JoinType.LEFT);

            if (brandId != null) predicates.add(cb.equal(brand.get("id"), brandId));
            if (modelId != null) predicates.add(cb.equal(model.get("id"), modelId));
            if (year != null) {
                predicates.add(cb.lessThanOrEqualTo(fit.get("yearFrom"), year));
                predicates.add(cb.greaterThanOrEqualTo(fit.get("yearTo"), year));
            }
            cq.distinct(true);
        }
        cq.where(predicates.toArray(new Predicate[0]));

        if ("price_asc".equalsIgnoreCase(sort)) {
            cq.orderBy(cb.asc(p.get("price")));
        } else if ("price_desc".equalsIgnoreCase(sort)) {
            cq.orderBy(cb.desc(p.get("price")));
        } else {
            cq.orderBy(cb.desc(p.get("id"))); // ✅ FIX
        }

        TypedQuery<Product> query = em.createQuery(cq);
        query.setFirstResult(page*size);
        query.setMaxResults(size);
        List<Product> products = query.getResultList();

        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        Root<Product> cp = countCq.from(Product.class);
        List<Predicate> countPreds = new ArrayList<>();
        countPreds.add(cb.isTrue(cp.get("isActive")));

        if(keyword!=null && !keyword.isBlank()){
            String like = "%" + keyword.trim().toLowerCase() + "%";
            countPreds.add(cb.or(
                    cb.like(cb.lower(cp.get("name")), like),
                    cb.like(cb.lower(cp.get("sku")), like)
            ));
        }
        if (categoryId != null) countPreds.add(cb.equal(cp.get("category").get("id"), categoryId));
        if (minPrice != null)
            countPreds.add(cb.greaterThanOrEqualTo(cp.get("price"), minPrice));
        if (maxPrice != null)
            countPreds.add(cb.lessThanOrEqualTo(cp.get("price"), maxPrice));

        if(brandId!=null || modelId!=null || year!=null){
            Join<Object, Object> cfit = cp.join("fitments", JoinType.INNER);
            Join<Object, Object> cmodel = cfit.join("carModel", JoinType.INNER);
            Join<Object, Object> cbrand = cmodel.join("brand", JoinType.INNER);

            if (brandId != null) countPreds.add(cb.equal(cbrand.get("id"), brandId));
            if (modelId != null) countPreds.add(cb.equal(cmodel.get("id"), modelId));
            if (year != null) {
                countPreds.add(cb.lessThanOrEqualTo(cfit.get("yearFrom"), year));
                countPreds.add(cb.greaterThanOrEqualTo(cfit.get("yearTo"), year));
            }
            countCq.select(cb.countDistinct(cp));
        }
        else {
            countCq.select(cb.count(cp));
        }
        countCq.where(countPreds.toArray(new Predicate[0]));
        Long total = em.createQuery(countCq).getSingleResult();

        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(products, pageable, total);
    }
}
