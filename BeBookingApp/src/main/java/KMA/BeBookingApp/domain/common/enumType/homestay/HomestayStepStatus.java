package KMA.BeBookingApp.domain.common.enumType.homestay;

import java.util.Optional;

public enum HomestayStepStatus {
    CREATED,         // Bước1 khởi tạo
    STYLE,           // Bước2 chọn phong cách
    SPACE,           // Bước3 chọn không gian
    AMENITY,         // Bước4 chọn tiện nghi
    MEDIA,          // Bước5 tải lên hình ảnh
    LOCATION,        // Bước6 chọn địa điểm
    INFORMATION,    // Bước7 điền thông tin
    CONFIRMED;         // Bước8 xác nhận thành công



    public Optional<HomestayStepStatus> getNextStatus() {
        int nextOrdinal = this.ordinal() + 1;
        if (nextOrdinal >= HomestayStepStatus.values().length) {
            return Optional.empty();
        }
        return Optional.of(HomestayStepStatus.values()[nextOrdinal]);
    }


    public Optional<HomestayStepStatus> getBeforeStatus() {
        int previousOrdinal = this.ordinal() - 1;
        return previousOrdinal >= 0
                ? Optional.of(HomestayStepStatus.values()[previousOrdinal])
                : Optional.empty();
    }
}
