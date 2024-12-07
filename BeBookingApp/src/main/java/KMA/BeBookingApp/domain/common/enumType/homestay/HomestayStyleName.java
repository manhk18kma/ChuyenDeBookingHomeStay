package KMA.BeBookingApp.domain.common.enumType.homestay;

public enum HomestayStyleName {

    HOME("Nhà", HomestayStyleType.RESIDENTIAL, "Kiểu nhà ở thông dụng, thường dành cho gia đình.", "https://tse4.mm.bing.net/th?id=OIP.zmER63lT5IFaHhS4RhzcxAHaHa&pid=Api&P=0&h=220"),
    APARTMENT("Căn hộ", HomestayStyleType.RESIDENTIAL, "Nhà ở dạng căn hộ, thường ở chung cư.", "https://tse3.mm.bing.net/th?id=OIP.YQUtYURfHzPy9GmpMT1S-QHaHa&pid=Api&P=0&h=220"),
    FARM_HOUSE("Nhà Nông trại", HomestayStyleType.RURAL, "Nhà ở nông thôn, thường có vườn và đất canh tác.", "https://tse2.mm.bing.net/th?id=OIP.ultAtfAKE3y5GRC0gILRHQHaHa&pid=Api&P=0&h=220"),
    BEACH_VILLA("Biệt thự Biển", HomestayStyleType.LUXURY, "Biệt thự gần bãi biển, lý tưởng cho kỳ nghỉ.", "https://tse4.mm.bing.net/th?id=OIP.xs6NegOcdTFHlVmr63NvaQHaHa&pid=Api&P=0&h=220"),
    MOUNTAIN_COTTAGE("Nhà Gỗ Núi", HomestayStyleType.UNIQUE, "Nhà gỗ nằm ở vùng núi, nơi nghỉ dưỡng tuyệt vời.", "https://cdn-icons-png.flaticon.com/128/15524/15524827.png"),
    TRADITIONAL_HOUSE("Nhà Truyền Thống", HomestayStyleType.UNIQUE, "Kiểu nhà mang đậm văn hóa và phong cách kiến trúc Việt Nam.", "https://tse4.mm.bing.net/th?id=OIP.qCumTyTCssVb4aSGERKHWAHaHa&pid=Api&P=0&h=220"),
    GUEST_HOUSE("Nhà Khách", HomestayStyleType.RESIDENTIAL, "Căn nhà cho thuê ngắn hạn, thường có dịch vụ ăn uống.", "https://tse3.mm.bing.net/th?id=OIP.dn3kSzGmNcBilpzJj8fJnwAAAA&pid=Api&P=0&h=220"),
    VILLA("Biệt thự", HomestayStyleType.LUXURY, "Nhà ở sang trọng, thường có khuôn viên rộng.", "https://tse1.mm.bing.net/th?id=OIP.XgauLZeDdkx_pevRfZgJYQHaHa&pid=Api&P=0&h=220"),
    CASTLE("Lâu đài", HomestayStyleType.UNIQUE, "Kiểu nhà ở mang đậm tính cổ điển và sang trọng.", "https://tse3.mm.bing.net/th?id=OIP.36ZbRkBKp6d7JyTNehTmzgHaH7&pid=Api&P=0&h=220"),
    CAVE("Hang động", HomestayStyleType.UNIQUE, "Nơi ở độc đáo trong các hang động.", "https://tse4.mm.bing.net/th?id=OIP.B99tiZbv1KM7kjZHdB1s0QHaHa&pid=Api&P=0&h=220"),
    TENT("Lều trại", HomestayStyleType.UNIQUE, "Nơi ở tạm thời, thường được sử dụng khi cắm trại.", "https://tse3.mm.bing.net/th?id=OIP.33Yp_CuLni-0wK3HPwg1_gHaGH&pid=Api&P=0&h=220"),
    HOUSEBOAT("Du thuyền", HomestayStyleType.UNIQUE, "Nhà ở di động trên nước.", "https://tse4.mm.bing.net/th?id=OIP.HigddBh_l3humu4dOLZXVAHaHa&pid=Api&P=0&h=220"),
    TREE_HOUSE("Nhà trên cây", HomestayStyleType.UNIQUE, "Nhà được xây dựng trên cây, tạo cảm giác gần gũi với thiên nhiên.", "https://tse2.mm.bing.net/th?id=OIP.Q3umM9JXoPUUDm_w6B2uGwHaHa&pid=Api&P=0&h=220"),
    CONTAINER_HOUSE("Nhà container", HomestayStyleType.UNIQUE, "Nhà được làm từ thùng container, kiểu dáng hiện đại và độc đáo.", "https://tse4.mm.bing.net/th?id=OIP.28v6GlZi1hrTYOS6suw18QAAAA&pid=Api&P=0&h=220"),
    MODERN_HOUSE("Nhà Hiện Đại", HomestayStyleType.RESIDENTIAL, "Nhà có thiết kế hiện đại, tiện nghi và thoải mái.", "https://tse1.mm.bing.net/th?id=OIP.DyG4TGVd5JcMIuL31_uQpAHaHa&pid=Api&P=0&h=220");

    private final String name;
    private final HomestayStyleType homestayStyleType;
    private final String description;
    private final String icon;

    // Constructor
    HomestayStyleName(String name, HomestayStyleType homestayStyleType, String description, String icon) {
        this.name = name;
        this.homestayStyleType = homestayStyleType;
        this.description = description;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public HomestayStyleType getHomestayStyleType() {
        return homestayStyleType;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}
