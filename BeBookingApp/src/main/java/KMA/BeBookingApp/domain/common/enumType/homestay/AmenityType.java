package KMA.BeBookingApp.domain.common.enumType.homestay;

public enum AmenityType {
    INDOOR("Tiện nghi trong nhà", "Tiện nghi có sẵn bên trong homestay, như điều hòa không khí và hệ thống sưởi."),
    OUTDOOR("Tiện nghi ngoài trời", "Tiện nghi có sẵn bên ngoài homestay, như khu vườn hoặc sân hiên."),
    ESSENTIALS("Tiện nghi cơ bản", "Các tiện nghi cơ bản cần thiết cho một kỳ nghỉ thoải mái, như đồ dùng vệ sinh."),
    ENTERTAINMENT("Giải trí", "Tiện nghi liên quan đến giải trí, bao gồm TV và máy chơi game."),
    SECURITY("An ninh", "Các tính năng nâng cao an ninh cho homestay, như hệ thống báo động hoặc camera."),
    SERVICE("Dịch vụ bổ sung", "Các dịch vụ bổ sung được cung cấp cho khách, như dọn dẹp hoặc dịch vụ lễ tân.");

    private final String name;
    private final String description;

    // Constructor
    AmenityType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for description
    public String getDescription() {
        return description;
    }
}
