package KMA.BeBookingApp.domain.booking.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class  HoldAvailabilityResponse {
    private Integer nights;
    private BigDecimal rateToUsd;
    private Long totalPriceVnd;
    private Long priceVndPerNight;
    private BigDecimal totalPriceUsd;
    private BigDecimal priceUsdPerNight;
}
