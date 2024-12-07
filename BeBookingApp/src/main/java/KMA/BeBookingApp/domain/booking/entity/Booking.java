package KMA.BeBookingApp.domain.booking.entity;

import KMA.BeBookingApp.domain.common.AbstractEntity;
import KMA.BeBookingApp.domain.common.enumType.booking.BookingStatus;
import KMA.BeBookingApp.domain.common.enumType.booking.HomestayAvailabilityStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking extends AbstractEntity<Long> {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "homestay_id")
    private Long homestayId;

    @Column(name = "checkin_date")
    private LocalDate checkinDate;

    @Column(name = "checkout_date")
    private LocalDate checkoutDate;

    private Integer nights;

    @Column(name = "guests")
    private Integer guests;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    BookingStatus bookingStatus;

    @Column(name = "note")
    private String note;

    BigDecimal amountUSD;

    Long amountVND;

    BigDecimal priceUSDPerNight;

    Long priceVNDPerNight;

}
