package KMA.BeBookingApp.domain.booking.service.abtract;

import KMA.BeBookingApp.domain.booking.dto.request.*;
import KMA.BeBookingApp.domain.booking.dto.response.HoldAvailabilityResponse;
import KMA.BeBookingApp.domain.booking.dto.response.HomestayAvailabilityResponse;
import KMA.BeBookingApp.domain.booking.dto.response.UpdateAvailabilityResponse;
import KMA.BeBookingApp.domain.common.enumType.booking.BookingStatus;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface HomestayAvailabilityService {
    void generateAvailabilityForNextYear(Long homestayId , Long defaultPrice);

    Map<YearMonth, List<HomestayAvailabilityResponse>> getAvailabilityByHomestayId(Long homestayId);

    UpdateAvailabilityResponse updateAvailability(Long homestayId, UpdateAvailabilityRequest request);


    UpdateAvailabilityResponse disableAvailability(Long homestayId, DisableAvailabilityRequest request);


    UpdateAvailabilityResponse enableAvailability(Long homestayId, EnableAvailabilityRequest request);

    HoldAvailabilityResponse checkAvailabilityForBooking(CheckAvailabilityRequest request);

    void markBooked(Long bookingId);

    void cancelPendingAvailabilityDay(Long bookingId);


    void runJobAtMidnightToUpdateRate();


}
