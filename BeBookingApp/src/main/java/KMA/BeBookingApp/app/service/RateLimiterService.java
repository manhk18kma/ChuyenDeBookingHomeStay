package KMA.BeBookingApp.app.service;

import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import KMA.BeBookingApp.domain.common.exception.AppException;
import KMA.BeBookingApp.domain.user.dto.response.FailedAttemptsResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RateLimiterService {



    private final StringRedisTemplate redisTemplate;

    /**
     * Checks if the request is allowed based on the rate limit.
     * @param request The incoming HTTP request.
     * @return true if the request is allowed, false if rate limit is exceeded.
     */
    public boolean isAllowed(HttpServletRequest request, RateLimitOption rateLimitOption) {
        String ipAddress = getClientIp(request);  // Lấy địa chỉ IP của client
        String key = rateLimitOption.getKey() + ipAddress;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        try {
            String value = ops.get(key);

            if (value == null) {
                ops.set(key, "1", rateLimitOption.getExpirationTime(), rateLimitOption.getTimeUnit());
                return true;
            }

            // Chuyển đổi giá trị từ Redis thành số nguyên
            int requestCount;
            try {
                requestCount = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // Nếu giá trị không hợp lệ, reset lại
                ops.set(key, "1", rateLimitOption.getExpirationTime(), rateLimitOption.getTimeUnit());
                return true;
            }

            // Kiểm tra giới hạn số lượng request
            if (requestCount >= rateLimitOption.getMaxRequests()) {
                return false;
            }

            // Tăng giá trị đếm lên 1
            ops.increment(key, 1);
            return true;
        } catch (Exception e) {
            // Xử lý lỗi Redis (có thể log lại)
            System.err.println("Error interacting with Redis: " + e.getMessage());
            return true; // Cho phép request nếu Redis gặp sự cố để tránh gián đoạn
        }
    }



    public String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    public RateLimitResponse getRateLimit(HttpServletRequest httpServletRequest , String typeAPi) {
        RateLimitOption rateLimitOption = RateLimitOption.valueOf(typeAPi);

        String ipAddress = getClientIp(httpServletRequest);  // Lấy địa chỉ IP client
        String key = rateLimitOption.getKey() + ipAddress;  // Key Redis
        ValueOperations<String, String> ops = redisTemplate.opsForValue();  // Redis String operations

        // Lấy số lần request từ Redis
        String value = ops.get(key);

        int remainingAttempts;
        if (value == null) {
            remainingAttempts = rateLimitOption.getMaxRequests();
        } else {
            int currentAttempts = Integer.parseInt(value);
            remainingAttempts = Math.max(0, rateLimitOption.getMaxRequests() - currentAttempts);
        }

        // Trả về thông tin FailedAttemptsResponse
        return new RateLimitResponse(
                rateLimitOption,
                ipAddress,
                remainingAttempts,
                rateLimitOption.getExpirationTime(),
                rateLimitOption.getTimeUnit().toString()
        );
    }

}
