package KMA.BeBookingApp.domain.homestay.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class SearchFilterResponse implements Serializable {

    private List<HomestayStyleResponse> styles;
    private List<AmenityResponse> amenities;
    private List<SpaceResponse> spaces;
    private PriceRangeResponse priceRange;
}
