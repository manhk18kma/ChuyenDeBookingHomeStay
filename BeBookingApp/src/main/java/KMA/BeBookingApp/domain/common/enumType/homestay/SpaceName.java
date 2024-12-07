package KMA.BeBookingApp.domain.common.enumType.homestay;

public enum SpaceName {

    LIVING_ROOM("Phòng khách", SpaceType.SHARED,"Không gian sinh hoạt chung.", "https://tse1.mm.bing.net/th?id=OIP.1NAv34oSsIjDJHy2GK80MgHaHa&pid=Api&P=0&h=220"),
    BEDROOM("Phòng ngủ", SpaceType.PRIVATE,"Không gian nghỉ ngơi riêng tư.", "https://tse3.mm.bing.net/th?id=OIP.9dw2PMYku8lrNEQwguXgAAHaHa&pid=Api&P=0&h=220"),
    KITCHEN("Nhà bếp", SpaceType.SHARED,"Không gian nấu nướng và ăn uống.", "https://tse2.mm.bing.net/th?id=OIP.cIHzEvtZa0pKSm7K-NO4SwHaHa&pid=Api&P=0&h=220"),
    BATHROOM("Phòng tắm", SpaceType.PRIVATE,"Không gian vệ sinh cá nhân.", "https://tse2.mm.bing.net/th?id=OIP.IcyD1uvdbnOZ0hR7NPF0YwHaHa&pid=Api&P=0&h=220"),
    BALCONY("Ban công",SpaceType.SHARED, "Không gian ngoài trời để thư giãn.", "https://tse3.mm.bing.net/th?id=OIP.Wl_k_nMVZClv2badG382pgHaHa&pid=Api&P=0&h=220"),
    DINING_ROOM("Phòng ăn",SpaceType.SHARED, "Không gian dùng bữa.", "https://tse2.mm.bing.net/th?id=OIP.KcWPpyySe8s8lpSdA-si-wHaHa&pid=Api&P=0&h=220"),
    STUDY_ROOM("Phòng làm việc", SpaceType.PRIVATE,"Không gian cho công việc và học tập.", "https://tse1.mm.bing.net/th?id=OIP.CRmPMMnQT__o8g42-0iYKAHaHa&pid=Api&P=0&h=220"),
    GARDEN("Khu vườn",SpaceType.OUTDOOR, "Không gian xanh mát để thư giãn.", "https://tse4.mm.bing.net/th?id=OIP.hrqvsieWUrtmZNtyl9JiFAHaHa&pid=Api&P=0&h=220"),
    GARAGE("Garage", SpaceType.INDOOR,"Khu vực để xe.", "https://tse1.mm.bing.net/th?id=OIP.LqDmXcvZQidsm7OW3PnTJQHaHa&pid=Api&P=0&h=220"),
    ROOFTOP("Mái nhà",SpaceType.INDOOR, "Không gian ngoài trời trên mái.", "https://tse1.mm.bing.net/th?id=OIP.W2ec0oRSffPV1twR0alHqQHaHa&pid=Api&P=0&h=220"),
    BASEMENT("Hầm", SpaceType.INDOOR,"Không gian lưu trữ dưới đất.", "https://tse3.mm.bing.net/th?id=OIP.CQpWuOKz18lspzouGVBq-wHaHa&pid=Api&P=0&h=220"),
    PLAYROOM("Phòng chơi",SpaceType.PRIVATE, "Không gian cho trẻ em chơi đùa.", "https://tse3.mm.bing.net/th?id=OIP.dcBA8R4KCbEoM69nx43U7AHaHa&pid=Api&P=0&h=220"),
    LAUNDRY_ROOM("Phòng giặt", SpaceType.INDOOR,"Không gian cho việc giặt giũ.", "https://tse3.mm.bing.net/th?id=OIP.E1lgFLTZkHHuD8LcRkKCvQAAAA&pid=Api&P=0&h=220"),
    STORAGE("Kho chứa",SpaceType.INDOOR, "Không gian lưu trữ.", "https://tse3.mm.bing.net/th?id=OIP.CgeF0-BVGMequv52aFy-rgHaHa&pid=Api&P=0&h=220"),
    FOYER("Lối vào",SpaceType.INDOOR, "Không gian chào đón khách.", "https://tse4.mm.bing.net/th?id=OIP.B8vH_uENO77mx2IX90u7EwAAAA&pid=Api&P=0&h=220"),
    PORCH("Hiên nhà",SpaceType.OUTDOOR, "Không gian phía trước nhà.", "https://tse1.mm.bing.net/th?id=OIP.bCARNzVRsjlwh8hhZvVBEwHaHa&pid=Api&P=0&h=220"),
    PATIO("Sân trong", SpaceType.OUTDOOR,"Không gian ngoài trời trong nhà.", "https://tse3.mm.bing.net/th?id=OIP.vE8FMWI6brbjlMo4RnBmFgHaHY&pid=Api&P=0&h=220"),
    HALL("Hội trường", SpaceType.COMMERCIAL,"Không gian lớn cho các sự kiện.", "https://tse2.mm.bing.net/th?id=OIP.VkQz8zf-Hby1U2C5zUl8mgHaHa&pid=Api&P=0&h=220"),
    CONFERENCE_ROOM("Phòng hội nghị", SpaceType.COMMERCIAL,"Không gian cho các cuộc họp.", "https://tse1.mm.bing.net/th?id=OIP.n5wagvz1Znwmfp9pTdZePAHaHa&pid=Api&P=0&h=220"),
    YOGA_STUDIO("Phòng yoga",SpaceType.INDOOR, "Không gian cho các bài tập yoga.", "https://tse1.mm.bing.net/th?id=OIP.fyhi43ZZCAFGj_7s9PaWogHaE8&pid=Api&P=0&h=220"),
    SPA("Spa", SpaceType.COMMERCIAL,"Không gian cho các liệu pháp spa.", "https://tse1.mm.bing.net/th?id=OIP.rj2Ws_oibPAhEddpMUkSHwHaH3&pid=Api&P=0&h=220"),
    GAME_ROOM("Phòng game", SpaceType.SHARED,"Không gian cho các trò chơi.", "https://tse1.mm.bing.net/th?id=OIP.FbDwajd8LBQFhYz8ds76cwHaHa&pid=Api&P=0&h=220"),
    DANCE_STUDIO("Phòng khiêu vũ", SpaceType.MULTIFUNCTIONAL,"Không gian cho các buổi khiêu vũ.", "https://tse3.mm.bing.net/th?id=OIP.L1BDO2wS7WzLSpfxrPH2lgHaHa&pid=Api&P=0&h=220");

    private final String name;
    private final SpaceType spaceType;
    private final String description;
    private final String icon;

    // Constructor

    SpaceName(String name, SpaceType spaceType, String description, String icon) {
        this.name = name;
        this.spaceType = spaceType;
        this.description = description;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public SpaceType getSpaceType() {
        return spaceType;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}
