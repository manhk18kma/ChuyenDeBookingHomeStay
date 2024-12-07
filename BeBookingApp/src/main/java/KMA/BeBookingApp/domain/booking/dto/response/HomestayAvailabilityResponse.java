package KMA.BeBookingApp.domain.booking.dto.response;

import KMA.BeBookingApp.domain.common.enumType.booking.HomestayAvailabilityStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class HomestayAvailabilityResponse {
    private Long id;
    private Long homestayId;
    private LocalDate date;
    private Long priceVND;
    private BigDecimal priceUSD;
    private HomestayAvailabilityStatus status;
    private String note;
    private BigDecimal rateToUsd;
    private Boolean isBeforeNow;


    public HomestayAvailabilityResponse(Long id, Long homestayId, LocalDate date, Long priceVND, BigDecimal priceUSD, HomestayAvailabilityStatus status, String note, BigDecimal rateToUsd, Boolean isBeforeNow) {
        this.id = id;
        this.homestayId = homestayId;
        this.date = date;
        this.priceVND = priceVND;
        this.priceUSD = priceUSD;
        this.status = status;
        this.note = note;
        this.rateToUsd = rateToUsd;
        this.isBeforeNow = isBeforeNow;
    }
}
