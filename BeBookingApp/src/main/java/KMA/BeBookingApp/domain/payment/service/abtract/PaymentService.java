package KMA.BeBookingApp.domain.payment.service.abtract;

import KMA.BeBookingApp.domain.common.dto.request.InitPaymentVnPayRequest;
import KMA.BeBookingApp.domain.common.dto.response.InitDepositResponse;

public interface PaymentService {
    InitDepositResponse initPayment(InitPaymentVnPayRequest request);

    Long markPaymentSuccess(Long vnpTxnRef);

    void cancelPayment(Long bookingId);
}
