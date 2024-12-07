package KMA.BeBookingApp.domain.common.enumType.homestay;

public enum HomestayStatus {
    DEFAULT("Mọi trạng thái" , "Tất cả các homestay các trạng thái , phục vụ cho query k được dùng làm write"),

    AVAILABLE("Có sẵn", "Homestay hiện đang có sẵn để đặt."),
    CREATING("Đang tạo", "Homestay đang trong quá trình đăng ký"),
    REMOVED("Đã xoá" , "Homestay đã bị xoá");

    private final String name;
    private final String description;

    // Constructor
    HomestayStatus(String name, String description) {
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
