package com.example.securitypractice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploader {
    private final Cloudinary cloudinary;

    public ImageUploader() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "ddd3ldsj2",
                "api_key", "944312699884881",
                "api_secret", "9RWx9g7uApQvhRcIMTp2cu6XFVo"
        ));
    }

    public String upload(MultipartFile file) {
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
