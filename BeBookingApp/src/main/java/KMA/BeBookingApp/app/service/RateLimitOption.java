package KMA.BeBookingApp.app.service;

import java.util.concurrent.TimeUnit;

public enum RateLimitOption {
    OUT_BOUND("rate_limit_outbound:", 10, TimeUnit.MINUTES, 100),
    CHANGE_PASSWORD("rate_limit_change_password:", 3, TimeUnit.MINUTES, 5),
    LOG_IN("rate_limit_login:", 1, TimeUnit.MINUTES, 5);

    private final String key;
    private final int expirationTime;
    private final TimeUnit timeUnit;
    private final int maxRequests;

    RateLimitOption(String key, int expirationTime, TimeUnit timeUnit, int maxRequests) {
        this.key = key;
        this.expirationTime = expirationTime;
        this.timeUnit = timeUnit;
        this.maxRequests = maxRequests;
    }

    public String getKey() {
        return key;
    }

    public int getExpirationTime() {
        return expirationTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public int getMaxRequests() {
        return maxRequests;
    }
}
