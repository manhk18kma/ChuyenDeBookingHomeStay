package KMA.BeBookingApp.domain.booking.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateAllPriceRequest {

    @NotEmpty(message = "List of availability IDs cannot be empty")
    private List<Long> availabilityIds;

    @NotNull(message = "PriceVND cannot be null")
    private Long priceVND;
}
