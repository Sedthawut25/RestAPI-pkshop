package com.pkshop.domain.promotion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PromotionCategoryId implements Serializable {

    @Column(name = "promotion_id")
    private Long promotionId;

    @Column(name = "category_id")
    private Long categoryId;
}