package KMA.BeBookingApp.domain.homestay.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AvgPriceResponse implements Serializable {
    private BigDecimal avgVndPerNight;
    private BigDecimal avgUsdPerNight;

    public AvgPriceResponse(Double avgPriceVND, Double avgPriceUSD) {
        this.avgVndPerNight = avgPriceVND != null ? BigDecimal.valueOf(avgPriceVND) : BigDecimal.ZERO;
        this.avgUsdPerNight = avgPriceUSD != null ? BigDecimal.valueOf(avgPriceUSD) : BigDecimal.ZERO;
    }
}
