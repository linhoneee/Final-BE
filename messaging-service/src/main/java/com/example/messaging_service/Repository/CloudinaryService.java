package com.example.messaging_service.Repository;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, Object> uploadFile(MultipartFile file, String resourceType) throws IOException {
        Map<String, Object> uploadParams = ObjectUtils.asMap(
                "resource_type", resourceType // 'image', 'video', hoặc 'auto'
        );
        return cloudinary.uploader().upload(file.getBytes(), uploadParams);
    }
}
