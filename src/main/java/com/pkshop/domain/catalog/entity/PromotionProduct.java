package com.pkshop.domain.catalog.entity;

import com.pkshop.domain.promotion.entity.Promotion;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="promotion_products")
@Getter @Setter
@NoArgsConstructor
public class PromotionProduct {

    @EmbeddedId
    private PromotionProductId id = new PromotionProductId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("promotionId")
    @JoinColumn(name="promotion_id", nullable=false)
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("productId")
    @JoinColumn(name="product_id", nullable=false)
    private Product product;
}