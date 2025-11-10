package iuh.student.www.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.enabled:false}")
    private boolean cloudinaryEnabled;

    /**
     * Upload ảnh lên Cloudinary
     * @param file MultipartFile
     * @param folder Folder trong Cloudinary (vd: "products")
     * @return URL của ảnh đã upload
     */
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        if (!cloudinaryEnabled) {
            log.warn("Cloudinary is not enabled. Skipping upload.");
            return null;
        }

        try {
            // Tạo public_id unique
            String publicId = folder + "/" + UUID.randomUUID().toString();

            // Upload lên Cloudinary với cấu hình đơn giản
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "folder", folder,
                            "resource_type", "image"
                    ));

            String imageUrl = (String) uploadResult.get("secure_url");
            log.info("Uploaded image to Cloudinary: {}", imageUrl);
            return imageUrl;

        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary: {}", e.getMessage());
            throw new IOException("Lỗi khi upload ảnh lên Cloudinary: " + e.getMessage(), e);
        }
    }

    /**
     * Xóa ảnh từ Cloudinary
     * @param imageUrl URL của ảnh cần xóa
     */
    public void deleteImage(String imageUrl) {
        if (!cloudinaryEnabled || imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        try {
            // Extract public_id từ URL
            // URL format: https://res.cloudinary.com/<cloud_name>/image/upload/v<version>/<public_id>.<format>
            String publicId = extractPublicIdFromUrl(imageUrl);

            if (publicId != null && !publicId.isEmpty()) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                log.info("Deleted image from Cloudinary: {}", publicId);
            }
        } catch (Exception e) {
            log.warn("Failed to delete image from Cloudinary: {}", e.getMessage());
            // Don't throw exception, just log warning
        }
    }

    /**
     * Extract public_id từ Cloudinary URL
     */
    private String extractPublicIdFromUrl(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return null;
        }

        try {
            // URL format: https://res.cloudinary.com/<cloud_name>/image/upload/v<version>/<folder>/<filename>.<ext>
            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2) {
                return null;
            }

            // Get everything after /upload/v<version>/
            String afterUpload = parts[1];
            // Remove version number (v1234567890/)
            if (afterUpload.matches("v\\d+/.*")) {
                afterUpload = afterUpload.substring(afterUpload.indexOf("/") + 1);
            }

            // Remove file extension
            int lastDot = afterUpload.lastIndexOf(".");
            if (lastDot > 0) {
                return afterUpload.substring(0, lastDot);
            }

            return afterUpload;
        } catch (Exception e) {
            log.warn("Failed to extract public_id from URL: {}", imageUrl);
            return null;
        }
    }

    /**
     * Kiểm tra xem Cloudinary có được enable không
     */
    public boolean isEnabled() {
        return cloudinaryEnabled;
    }
}
