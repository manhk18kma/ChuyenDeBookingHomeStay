package KMA.BeBookingApp.domain.booking.dto.request;

import KMA.BeBookingApp.domain.common.enumType.booking.HomestayAvailabilityStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateAvailabilityRequest implements Serializable {

    @NotNull(message = "Giá VND không được để trống.")
    @Min(value = 50000, message = "Giá VND phải lớn hơn hoặc bằng 50,000.")
    private Long priceVND;


    @Size(max = 500, message = "Ghi chú không được dài quá 500 ký tự.")  // Giới hạn độ dài ghi chú
    private String note;

    @NotEmpty(message = "Danh sách availabilityIds không được để trống.")
    private List<Long> availabilityIds;
}
