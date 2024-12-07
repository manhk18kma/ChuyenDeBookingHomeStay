package KMA.BeBookingApp.domain.homestay.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class UpdateHomestaySpaceRequest implements Serializable {
    @NotEmpty(message = "Danh sách spaceIds không được để trống.")
    private List<@NotNull(message = "Phần tử trong danh sách spaceIds không được null.")
    @Positive(message = "ID tiện ích phải là số dương")Long> spaceIds;
}
