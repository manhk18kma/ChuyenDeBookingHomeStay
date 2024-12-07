package KMA.BeBookingApp.domain.booking.dto.response;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public class UpdateAvailabilityResponse implements Serializable {
    Long homestayId;
    List<Long>  effectedIds;
}
