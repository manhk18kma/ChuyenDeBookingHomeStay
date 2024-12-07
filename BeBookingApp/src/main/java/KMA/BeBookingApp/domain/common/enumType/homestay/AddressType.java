package KMA.BeBookingApp.domain.common.enumType.homestay;

public enum AddressType {
    HOME("Nhà", "Địa chỉ nhà ở của cá nhân."),
    WORK("Công ty", "Địa chỉ nơi làm việc."),
    VACATION("Kỳ nghỉ", "Địa chỉ cho kỳ nghỉ."),
    OTHER("Khác", "Các loại địa chỉ khác không thuộc các loại trên.");

    private final String name;
    private final String description;

    // Constructor
    AddressType(String name, String description) {
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
