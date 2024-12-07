package KMA.BeBookingApp.domain.booking.service.abtract;

import KMA.BeBookingApp.domain.booking.dto.request.BookingRequest;
import KMA.BeBookingApp.domain.booking.dto.response.BookingResponse;
import KMA.BeBookingApp.domain.booking.entity.Booking;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);

    void markBooked(Long bookingId);

    void cancelCronJobBooking(Long bookingId);

    void cancelBooking(Long bookingId);

    void updateCompletedBookings();

}
