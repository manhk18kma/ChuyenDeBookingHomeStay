package KMA.BeBookingApp.domain.booking.entity;

import KMA.BeBookingApp.domain.common.AbstractEntity;
import KMA.BeBookingApp.domain.common.enumType.booking.HomestayAvailabilityStatus;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStepStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "homestay_availability")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomestayAvailability extends AbstractEntity<Long> {
    @Column(name = "homestay_id")
    private Long homestayId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "priceVND")
    private Long priceVND;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "priceUSD")
    private BigDecimal priceUSD;

    @Enumerated(EnumType.STRING)
    @Column(name = "homestay_availability_status")
    HomestayAvailabilityStatus homestayAvailabilityStatus;

    private String note;

    private Long bookingId;

}
