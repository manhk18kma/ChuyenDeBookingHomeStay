package KMA.BeBookingApp.domain.homestay.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Builder
@Data
@ToString
public class SearchHomestayRequest {
    private Double latitude;
    private Double longitude;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Integer guests;



    private Long styleId;
    private List<Long> spaceIds;
    private List<Long> amenityIds;
    private Long minPriceVnd;
    private Long maxPriceVnd;
}
