package KMA.BeBookingApp.domain.common.dto.request;

import KMA.BeBookingApp.domain.common.enumType.payment.PaymentMethod;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class InitPaymentVnPayRequest {
    private String ipAddress;
    private Long bookingId;
    Long amountVND;
}
