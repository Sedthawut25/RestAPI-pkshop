package com.pkshop.api.common;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pkshop.Cloudinary.ImageUploadService;
import com.pkshop.common.response.ApiResponse;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private final ImageUploadService imageUploadService;

    public FileUploadController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping({"/claim-image", "/image"})
    public ApiResponse<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = imageUploadService.uploadImage(file);
        return ApiResponse.ok("อัปโหลดรูปสำเร็จ", imageUrl);
    }
}
