package KMA.BeBookingApp.domain.common.service;

import KMA.BeBookingApp.domain.common.enumType.homestay.MediaType;
import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import KMA.BeBookingApp.domain.common.exception.AppException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

    final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {
        String publicId = UUID.randomUUID().toString();
        String url;
        try {
            // Cấu hình các tham số cho Cloudinary
            Map<String, Object> params = ObjectUtils.asMap(
                    "public_id", publicId,
                    "overwrite", true,
                    "resource_type", "auto",
                    "secure", true

            );

            // Tải file lên Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
            url = uploadResult.get("url").toString();

        } catch (Exception e) {
            // Xử lý lỗi tải lên Cloudinary
            throw new AppException(AppErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        return url.replace("http", "https");
    }
//            Map<String, Object> params = ObjectUtils.asMap("resource_type", "auto"); // Tự động nhận diện tài nguyên

    public void deleteCloud(String url) {
        try {
            String publicId = getPublicId(url);
            String resourceType = getResourceTypeFromUrl(url);

            // Cấu hình tham số để xóa
            Map<String, Object> params = ObjectUtils.asMap("resource_type", resourceType);
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, params);
            System.out.println("Kết quả xóa: " + result);
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper function
    private String getPublicId(String url) {
        String[] parts = url.split("/");
        String publicIDWithFormat = parts[parts.length - 1];
        String[] publicIDParts = publicIDWithFormat.split("\\.");
        return publicIDParts[0];
    }

    private String getResourceTypeFromUrl(String url) {
        if (url.endsWith(".mp4") || url.endsWith(".mov") || url.endsWith(".avi")) {
            return "video";
        } else if (url.endsWith(".mp3") || url.endsWith(".wav")) {
            return "audio";
        } else if (url.endsWith(".pdf") || url.endsWith(".docx") || url.endsWith(".xlsx")) {
            return "raw"; // "raw" dành cho các file không phải image hoặc video
        } else {
            return "image"; // Mặc định là image
        }
    }

    @Data
    @Builder
    public static class UploadResponse {
        private String urlCloud;
        private String publicId;
    }

}
