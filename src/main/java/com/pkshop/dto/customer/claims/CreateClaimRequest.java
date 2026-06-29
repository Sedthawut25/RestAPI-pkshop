package com.pkshop.dto.customer.claims;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data

public class CreateClaimRequest {

    @NotNull(message = "กรุณาระบบรหัสออเดอร์")
    private Long orderId;

    @NotNull(message = "กรุณาระบบรหัสสินค้า")
    private Long productId;

    @NotNull(message = "กรุณาระบุจำนวน")
    @Min(value = 1, message = "จำนวนต้องไม่น้อยกว่า 1")
    private Integer quantity;

    @NotBlank(message = "กรุณาระบุประเภทการเคลม")
    private String claimType;

    @NotBlank(message = "กรุณาระบุเหตุผล")
    private String description;

    private List<String> fileUrls;
}
