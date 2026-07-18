package com.pkshop.dto.customer.claims;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateClaimRequest {

    @NotNull(message = "กรุณาระบุ Order ID")
    private Long orderId;

    @NotNull(message = "กรุณาระบุ Product ID")
    private Long productId;

    private String productName;

    @Min(value = 1, message = "จำนวนที่เคลมต้องมากกว่า 0")
    private Integer quantity;

    @NotBlank(message = "กรุณาระบุความต้องการ (เช่น คืนเงิน, เปลี่ยนสินค้า)")
    private String claimType;

    @NotBlank(message = "กรุณาระบุเหตุผล")
    private String description;

    // ลิงก์รูปภาพที่อัปโหลดสำเร็จแล้ว (ส่งมาจากหน้าบ้าน)
    private String imageUrl;
}