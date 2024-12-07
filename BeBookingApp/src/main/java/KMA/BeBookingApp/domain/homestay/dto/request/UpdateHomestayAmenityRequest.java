package KMA.BeBookingApp.domain.homestay.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class UpdateHomestayAmenityRequest implements Serializable {
    @NotEmpty(message = "Danh sách tiện ích không được để trống")
    private List<@NotNull(message = "ID tiện ích không được null")
    @Positive(message = "ID tiện ích phải là số dương") Long> amenityIds;
}
