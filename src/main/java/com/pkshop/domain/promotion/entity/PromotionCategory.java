package com.pkshop.domain.promotion.entity;

import com.pkshop.domain.catalog.entity.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "promotion_categories",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_promo_cat",
                columnNames = {"promotion_id", "category_id"}
        )
)
@Getter @Setter
@NoArgsConstructor
public class PromotionCategory {

    @EmbeddedId
    private PromotionCategoryId id = new PromotionCategoryId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("promotionId")
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}