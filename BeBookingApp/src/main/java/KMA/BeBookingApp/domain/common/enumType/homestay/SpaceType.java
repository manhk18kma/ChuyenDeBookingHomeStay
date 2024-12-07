package KMA.BeBookingApp.domain.common.enumType.homestay;

public enum SpaceType {

    PRIVATE("Riêng tư", "Không gian riêng biệt cho cá nhân hoặc nhóm nhỏ."),
    SHARED("Chia sẻ", "Không gian dùng chung với những người khác."),
    OUTDOOR("Ngoài trời", "Không gian ở bên ngoài, thường được sử dụng cho các hoạt động giải trí."),
    INDOOR("Trong nhà", "Không gian bên trong, thường được sử dụng cho các hoạt động sinh hoạt hàng ngày."),
    COMMERCIAL("Thương mại", "Không gian được sử dụng cho mục đích kinh doanh hoặc thương mại."),
    MULTIFUNCTIONAL("Đa chức năng", "Không gian có thể được sử dụng cho nhiều mục đích khác nhau.");

    private final String name;
    private final String description;

    // Constructor
    SpaceType(String name, String description) {
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
