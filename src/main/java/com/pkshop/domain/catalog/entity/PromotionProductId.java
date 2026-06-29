package com.pkshop.domain.catalog.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class PromotionProductId implements Serializable {
    private Long promotionId;
    private Long productId;
}