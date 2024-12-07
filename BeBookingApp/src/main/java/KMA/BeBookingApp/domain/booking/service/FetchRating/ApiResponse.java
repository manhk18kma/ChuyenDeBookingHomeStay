package KMA.BeBookingApp.domain.booking.service.FetchRating;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;


}
