package KMA.BeBookingApp.domain.payment.dto.request;

import KMA.BeBookingApp.domain.common.enumType.payment.PaymentMethod;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InitDepositPayRequest {
    private String ipAddress;

    private Long txnRef;

    BigDecimal amountUSD;

    BigDecimal rateToUsd;

    Long amountVND;

}
