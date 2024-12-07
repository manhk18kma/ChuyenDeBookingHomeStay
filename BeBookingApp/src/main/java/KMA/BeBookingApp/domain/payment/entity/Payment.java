package KMA.BeBookingApp.domain.payment.entity;

import KMA.BeBookingApp.domain.common.AbstractEntity;
import KMA.BeBookingApp.domain.common.enumType.payment.PaymentMethod;
import KMA.BeBookingApp.domain.common.enumType.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment extends AbstractEntity<Long> {

    @Column(name = "amount_usd")
    BigDecimal amountUSD;

    @Column(name = "rate_to_usd")
    BigDecimal rateToUsd;

    @Column(name = "amount_vnd")
    Long amountVND;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    PaymentMethod paymentMethod;


    @Column(name = "booking_id")
    Long bookingId;

    LocalDateTime expiredAt;


}
