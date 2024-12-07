package KMA.BeBookingApp.domain.homestay.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UpdateHomestayStyleRequest implements Serializable {
    @NotNull(message = "homestayStyleId không được để trống")
    private Long homestayStyleId;
}
