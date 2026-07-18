package com.pkshop.api.common;

import com.pkshop.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @PostMapping("/claim-image")
    public ApiResponse<String> uploadClaimImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ApiResponse<>(false,"กรุณาเลือกไฟล์รูปภาพ", null);
        }

        try {
            String uploadDirStr = System.getProperty("user.dir") + File.separator + "upload" + File.separator + "claims" + File.separator;
            File uploadDir = new File(uploadDirStr);

            if(!uploadDir.exists()) {

                boolean isCreated = uploadDir.mkdirs();
                if(!isCreated && !uploadDir.exists()) {
                    log.error("ไม่สามรถสร้างโฟลเดอร์เก็บรูปภาพได้: {}", uploadDirStr);
                }
            }

            String originFileName = file.getOriginalFilename();
            String extension = ".jpg";
            if(originFileName != null && originFileName.contains(".")) {
                extension = originFileName.substring(originFileName.lastIndexOf("."));
            }

            String newFilename = UUID.randomUUID() + extension;

            File destFile = new File(uploadDirStr + newFilename);
            file.transferTo(destFile);

            String fileUrl = "http://localhost:8080/upload/claims/" + newFilename;
            return ApiResponse.ok("อัปโหลดรูปสำเร็จ", fileUrl);

        }
        catch (IOException e) {
            log.error("เกิดข้อผิดพลาดขณะบันทึกไฟล์รูปภาพ", e);
            return new ApiResponse<>(false,"เกิดข้อผิดพลาดขณะบันทึกไฟล์ภาพ: " + e.getMessage(), null);
        }
    }
}
