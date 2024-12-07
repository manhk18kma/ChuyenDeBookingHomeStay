package KMA.BeBookingApp.domain.common.enumType.homestay;

public enum HomestayStyleType {
    RESIDENTIAL("Nhà ở"),
    COMMERCIAL("Thương mại"),
    RURAL("Nông thôn"),
    LUXURY("Sang trọng"),
    UNIQUE("Độc đáo");

    private final String typeName;

    HomestayStyleType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
