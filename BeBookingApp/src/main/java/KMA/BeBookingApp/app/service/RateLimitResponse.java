package KMA.BeBookingApp.app.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateLimitResponse {
    private RateLimitOption rateLimitOption;
    private String ipAddress;      // Địa chỉ IP của client
    private int remainingAttempts; // Số lần request còn lại
    private int expirationTime;    // Thời gian hết hạn (ví dụ: 1 phút)
    private String expirationUnit; // Đơn vị thời gian (e.g., MINUTES)
}
