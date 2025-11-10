package iuh.student.www.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Service để xử lý upload và lưu trữ file ảnh
 * Lưu file vào thư mục static/uploads/ để có thể serve qua web
 */
@Service
@Slf4j
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir:src/main/resources/static/uploads/products}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("✅ Created upload directory: {}", this.fileStorageLocation);
        } catch (Exception ex) {
            log.error("❌ Could not create upload directory!", ex);
            throw new RuntimeException("Could not create upload directory!", ex);
        }
    }

    /**
     * Lưu file ảnh và trả về URL để lưu vào database
     */
    public String storeProductImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // Validate file
            validateImageFile(file);

            // Generate unique filename
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = getFileExtension(originalFilename);
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            // Copy file to target location
            Path targetLocation = this.fileStorageLocation.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            log.info("✅ Saved image: {} → {}", originalFilename, newFilename);

            // Return web-accessible URL
            return "/uploads/products/" + newFilename;

        } catch (IOException ex) {
            log.error("❌ Failed to store file: {}", file.getOriginalFilename(), ex);
            throw new RuntimeException("Lỗi khi lưu file ảnh: " + file.getOriginalFilename(), ex);
        }
    }

    /**
     * Xóa file ảnh cũ khi update product
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        try {
            // Extract filename from URL: /uploads/products/xxx.jpg → xxx.jpg
            String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            Path filePath = this.fileStorageLocation.resolve(filename);

            Files.deleteIfExists(filePath);
            log.info("🗑️ Deleted old image: {}", filename);

        } catch (IOException ex) {
            log.warn("⚠️ Failed to delete file: {}", fileUrl, ex);
            // Don't throw exception, just log warning
        }
    }

    /**
     * Validate file ảnh
     */
    private void validateImageFile(MultipartFile file) {
        // Check file size (max 5MB)
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw new RuntimeException("Kích thước file vượt quá giới hạn 5MB");
        }

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Chỉ cho phép upload file ảnh (JPG, PNG, GIF)");
        }

        // Check filename
        String filename = file.getOriginalFilename();
        if (filename == null || filename.contains("..")) {
            throw new RuntimeException("Tên file không hợp lệ");
        }
    }

    /**
     * Get file extension from filename
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
