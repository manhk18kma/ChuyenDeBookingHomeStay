package KMA.BeBookingApp.domain.booking.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class CheckAvailabilityRequest {
    Long bookingId;
    Long homestayId;
    LocalDate checkInDate;
    LocalDate checkoutDate;
}
