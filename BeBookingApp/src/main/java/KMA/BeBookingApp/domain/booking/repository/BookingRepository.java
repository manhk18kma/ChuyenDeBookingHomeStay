package KMA.BeBookingApp.domain.booking.repository;

import KMA.BeBookingApp.domain.booking.entity.Booking;
import KMA.BeBookingApp.domain.common.enumType.booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking , Long> {

    @Modifying
    @Query("UPDATE Booking b SET b.bookingStatus = :booked WHERE b.id = :bookingId AND b.bookingStatus = 'PENDING'")
    int markBooked(@Param("bookingId") Long bookingId, BookingStatus booked);


    @Modifying
    @Query("UPDATE Booking b SET b.bookingStatus = 'CANCELLED' WHERE b.id = :bookingId AND b.bookingStatus = 'PENDING'")
    int cancelCronJobBooking(Long bookingId);


    @Modifying
    @Query("""
    UPDATE Booking b 
    SET b.bookingStatus = 'CANCELLED'
    WHERE 
        b.id = :bookingId
        AND b.bookingStatus = 'PENDING'
        AND b.userId = :id
""")
    int cancelBooking(@Param("bookingId") Long bookingId, @Param("id") Long id);

    @Query("""
SELECT case WHEN count(b) >= :maxBookingPerDay THEN true ELSE false END
FROM Booking b
WHERE b.userId = :id
AND b.createdAt >= :startOfDay
AND b.createdAt < :endOfDay
""")
    boolean isMaxBookingPerDay(@Param("id") Long id,
                               @Param("maxBookingPerDay") int maxBookingPerDay,
                               @Param("startOfDay") LocalDateTime startOfDay,
                               @Param("endOfDay") LocalDateTime endOfDay);


    @Modifying
    @Query("""
    UPDATE Booking b 
    SET b.bookingStatus = :completed 
    WHERE b.checkoutDate <= :currentDate 
      AND b.bookingStatus = :inProgress
""")
    int markBookingsCompleted(@Param("currentDate") LocalDate currentDate,
                              @Param("completed") BookingStatus completed,
                              @Param("inProgress") BookingStatus inProgress);





}
