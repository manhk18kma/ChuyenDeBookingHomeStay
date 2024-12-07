package KMA.BeBookingApp.domain.homestay.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceRangeResponse {

    private Long minPriceVND;
    private Long maxPriceVND;
    private BigDecimal minPriceUSD;
    private BigDecimal maxPriceUSD;
    private BigDecimal rate;

    public PriceRangeResponse(Long minPriceVND, Long maxPriceVND, BigDecimal minPriceUSD, BigDecimal maxPriceUSD) {
        this.minPriceVND = minPriceVND;
        this.maxPriceVND = maxPriceVND;
        this.minPriceUSD = minPriceUSD;
        this.maxPriceUSD = maxPriceUSD;
    }
}
