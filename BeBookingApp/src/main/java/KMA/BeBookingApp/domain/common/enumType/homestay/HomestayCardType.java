package KMA.BeBookingApp.domain.common.enumType.homestay;

public enum HomestayCardType {
    IN_PROCESS("Đang cập nhật", "Homestay đang yêu cầu thêm thông tin khởi tạo"),
    CREATED("Đã tạo", "Homestay đã được tạo và sẵn sàng lên lịch cho thuê");

    private final String name;
    private final String description;

    HomestayCardType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
