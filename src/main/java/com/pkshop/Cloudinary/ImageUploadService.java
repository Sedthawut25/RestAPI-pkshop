package com.pkshop.Cloudinary;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pkshop.common.exception.BadRequestException;

@Service
public class ImageUploadService {

    private final Cloudinary cloudinary;

    public ImageUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("กรุณาเลือกไฟล์รูปภาพ");
        }

        String contentType = file.getContentType();
        if (contentType != null && !contentType.startsWith("image/")) {
            throw new BadRequestException("รองรับเฉพาะไฟล์รูปภาพ");
        }

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "image",
                            "folder", "pkshop"
                    )
            );

            Object secureUrl = uploadResult.get("secure_url");
            if (secureUrl != null && !secureUrl.toString().isBlank()) {
                return secureUrl.toString();
            }

            Object url = uploadResult.get("url");
            if (url == null || url.toString().isBlank()) {
                throw new BadRequestException("Cloudinary ไม่ส่ง URL กลับมา");
            }
            return url.toString();
        } catch (IOException e) {
            throw new BadRequestException("เกิดข้อผิดพลาดขณะอัปโหลดรูปภาพ: " + e.getMessage());
        }
    }
}
