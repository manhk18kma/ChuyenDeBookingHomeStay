package KMA.BeBookingApp.domain.common.enumType.homestay;

public enum AmenityName {

    WIFI("Wifi", AmenityType.ENTERTAINMENT,"Kết nối Internet không dây.", "https://tse1.mm.bing.net/th?id=OIP.B2TVwfcYWrqJcLmpOTyPxwHaHa&pid=Api&P=0&h=220"),
    PARKING("Bãi đậu xe", AmenityType.ENTERTAINMENT,"Khu vực đậu xe an toàn.", "https://tse2.mm.bing.net/th?id=OIP.4O2h5eWG1FdJEh_LpW4BAwHaHa&pid=Api&P=0&h=220"),
    POOL("Hồ bơi",AmenityType.ENTERTAINMENT, "Hồ bơi cho khách sử dụng.", "https://tse1.mm.bing.net/th?id=OIP.pV63DSTB0oh94YETpLM1KQHaHa&pid=Api&P=0&h=220"),
    AIR_CONDITIONING("Điều hòa không khí", AmenityType.SERVICE,"Hệ thống điều hòa không khí cho không gian mát mẻ.", "https://tse1.mm.bing.net/th?id=OIP.oICCr-3wOafs-g-sSbkQdQHaHa&pid=Api&P=0&h=220"),
    HEATING("Sưởi ấm", AmenityType.SERVICE,"Hệ thống sưởi ấm cho không gian ấm áp.", "https://tse2.mm.bing.net/th?id=OIP.RgVSHK_lJdMDTuYTFKsh-gHaHa&pid=Api&P=0&h=220"),
    KITCHEN("Nhà bếp",AmenityType.SERVICE, "Khu vực bếp được trang bị đầy đủ.", "https://tse2.mm.bing.net/th?id=OIP.cIHzEvtZa0pKSm7K-NO4SwHaHa&pid=Api&P=0&h=220"),
    GYM("Phòng tập thể dục", AmenityType.SERVICE,"Khu vực tập thể dục cho khách.", "https://tse3.mm.bing.net/th?id=OIP.cAuQKlnmLCcjpfVwgbKgSAHaHa&pid=Api&P=0&h=220"),
    BBQ("Tiệc nướng BBQ",AmenityType.SERVICE, "Khu vực tiệc nướng ngoài trời.", "https://tse4.mm.bing.net/th?id=OIP.xcIc_EozS64xBWS9ikkEIQHaJ4&pid=Api&P=0&h=220"),
    PET_FRIENDLY("Cho phép thú cưng",AmenityType.SERVICE, "Chấp nhận thú cưng của khách.", "https://tse1.mm.bing.net/th?id=OIP.orZtK4ybh7x09SLirO3XZQHaHa&pid=Api&P=0&h=220"),
    BALCONY("Ban công", AmenityType.OUTDOOR,"Khu vực ban công riêng.", "https://static.thenounproject.com/png/990247-200.png"),
    TV("Truyền hình",AmenityType.ENTERTAINMENT, "TV với các kênh truyền hình cáp.", "https://tse1.mm.bing.net/th?id=OIP.1GgNM0G06amx09dcr8a0-wHaE8&pid=Api&P=0&h=220"),
    LAUNDRY("Giặt là", AmenityType.SERVICE,"Dịch vụ giặt là cho khách.", "https://tse3.mm.bing.net/th?id=OIP.E1lgFLTZkHHuD8LcRkKCvQAAAA&pid=Api&P=0&h=220"),
    SECURITY("An ninh", AmenityType.SECURITY,"Hệ thống an ninh 24/7.", "https://tse3.mm.bing.net/th?id=OIP.7owQZvT0_adHcXIFyP8w9AHaJI&pid=Api&P=0&h=220"),
    SPA("Spa",AmenityType.SERVICE, "Dịch vụ chăm sóc sức khỏe và làm đẹp.", "https://tse1.mm.bing.net/th?id=OIP.rj2Ws_oibPAhEddpMUkSHwHaH3&pid=Api&P=0&h=220"),
    LIFT("Thang máy", AmenityType.INDOOR,"Thang máy tiện lợi cho các tầng.", "https://tse2.mm.bing.net/th?id=OIP.GisdtALM8IS_HouqSqw64gHaHa&pid=Api&P=0&h=220"),
    GARDEN("Khu vườn",AmenityType.OUTDOOR, "Khu vực vườn xanh mát.", "https://tse4.mm.bing.net/th?id=OIP.hrqvsieWUrtmZNtyl9JiFAHaHa&pid=Api&P=0&h=220"),
    SMOKING_AREA("Khu vực hút thuốc", AmenityType.SERVICE,"Khu vực riêng cho khách hút thuốc.", "https://tse2.mm.bing.net/th?id=OIP.TvjebP-1szMcnpQw7Qma4wHaHa&pid=Api&P=0&h=220"),
    FIREPLACE("Lò sưởi",AmenityType.ESSENTIALS, "Lò sưởi ấm cúng.", "https://tse2.mm.bing.net/th?id=OIP.LXRQP1pn2jpMVaDdTO98JgHaHa&pid=Api&P=0&h=220"),
    COFFEE_MACHINE("Máy pha cà phê",AmenityType.ESSENTIALS, "Máy pha cà phê tiện lợi.", "https://tse3.mm.bing.net/th?id=OIP.tKf9XIQ7Jy5O-Vhr2NxEaAHaHa&pid=Api&P=0&h=220"),
    DISHWASHER("Máy rửa bát",AmenityType.ESSENTIALS, "Máy rửa bát tự động.", "https://tse4.mm.bing.net/th?id=OIP.R7Mx2P4VU9Pi5-v7Ew7WXQHaHa&pid=Api&P=0&h=220"),
    MICROWAVE("Lò vi sóng",AmenityType.ESSENTIALS, "Lò vi sóng cho việc nấu nướng nhanh.", "https://tse4.mm.bing.net/th?id=OIP.O08G3dxjpWxLe2n0hb5nFAHaHa&pid=Api&P=0&h=220"),
    GAME_CONSOLE("Máy chơi game",AmenityType.ENTERTAINMENT, "Máy chơi game cho khách giải trí.", "https://tse3.mm.bing.net/th?id=OIP.9hgBu2haApJffrJ9LD9HPwHaEp&pid=Api&P=0&h=220"),
    DVD_PLAYER("Máy chiếu DVD",AmenityType.ENTERTAINMENT, "Máy chiếu DVD cho việc giải trí.", "https://tse2.mm.bing.net/th?id=OIP.zU3mx7mbVUBuAQqY7UE8ZQHaHa&pid=Api&P=0&h=220"),
    JACUZZI("Bồn tắm sục", AmenityType.ESSENTIALS,"Bồn tắm sục cho trải nghiệm thư giãn.", "https://tse4.mm.bing.net/th?id=OIP.kImMfC4Vp95hlPt3zpm16gHaH7&pid=Api&P=0&h=220"),
    FIREPIT("Bếp lửa", AmenityType.ESSENTIALS,"Khu vực bếp lửa ngoài trời.", "https://tse4.mm.bing.net/th?id=OIP.-gaoNZXxtLKtTKVcScRUlQHaHa&pid=Api&P=0&h=220"),
    SNOW_SPORTS("Thể thao mùa đông", AmenityType.ENTERTAINMENT,"Thiết bị cho các hoạt động thể thao mùa đông.", "https://tse2.mm.bing.net/th?id=OIP.w9EDMgwU9KWNMTL-JK02TwHaE7&pid=Api&P=0&h=220"),
    AIRPORT_TRANSFER("Dịch vụ đưa đón sân bay",AmenityType.SERVICE, "Dịch vụ đưa đón sân bay cho khách.", "https://tse2.mm.bing.net/th?id=OIP.3kkB-uf0dQ86zO17as_CHAHaG9&pid=Api&P=0&h=220");

    private final String name;
    private final String description;
    private final String icon;

    private final AmenityType amenityType;


    // Constructor


    AmenityName(String name, AmenityType amenityType, String description, String icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.amenityType = amenityType;
    }

    // Getter for name

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public AmenityType getAmenityType() {
        return amenityType;
    }
}
