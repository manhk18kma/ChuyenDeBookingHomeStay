package KMA.BeBookingApp.domain.booking.dto.request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class DisableAvailabilityRequest implements Serializable {
    @NotEmpty(message = "Danh sách availabilityIds không được để trống.")
    private List<Long> availabilityIds;
}
