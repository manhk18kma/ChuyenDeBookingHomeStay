package KMA.BeBookingApp.domain.payment.service.impl;

import KMA.BeBookingApp.domain.booking.service.abtract.BookingService;
import KMA.BeBookingApp.domain.common.enumType.payment.PaymentStatus;
import KMA.BeBookingApp.domain.common.exception.AppException;
import KMA.BeBookingApp.domain.payment.constant.VNPayParams;
import KMA.BeBookingApp.domain.payment.constant.VnpIpnResponseConst;
import KMA.BeBookingApp.domain.payment.dto.response.IpnResponse;
import KMA.BeBookingApp.domain.payment.dto.response.VnpayResponse;
import KMA.BeBookingApp.domain.payment.entity.Payment;
import KMA.BeBookingApp.domain.payment.repository.PaymentRepository;
import KMA.BeBookingApp.domain.payment.service.abtract.IpnService;
import KMA.BeBookingApp.domain.payment.service.abtract.PaymentService;
import KMA.BeBookingApp.domain.payment.service.vnpay.VnpayService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IpnServiceImpl implements IpnService {
    PaymentRepository paymentRepository;
    BookingService bookingService;
    VnpayService vnpayService;
    PaymentService paymentService;

    @Override
    public IpnResponse handlePaymentGatewayCallback(Map<String, String> params) {

        if (!vnpayService.verifyIpn(params)) {
            log.error("[VNPay Ipn] Invalid IPN received. Signature verification failed.");
            return VnpIpnResponseConst.SIGNATURE_FAILED;
        }

        VnpayResponse vnpayResponse = new VnpayResponse(params);
        IpnResponse response;

        try {
            Long bookingId = paymentService.markPaymentSuccess(Long.valueOf(vnpayResponse.getVnpTxnRef()));
            bookingService.markBooked(bookingId);
            response = VnpIpnResponseConst.SUCCESS;
        } catch (AppException e) {
            switch (e.getErrorCode()) {
                case BOOKING_NOT_FOUND:
                    log.error("[VNPay Ipn] Booking not found for txnRef: {}", vnpayResponse.getVnpTxnRef());
                    response = VnpIpnResponseConst.ORDER_NOT_FOUND;
                    break;
                default:
                    log.error("[VNPay Ipn] Unknown AppException occurred for txnRef: {}", vnpayResponse.getVnpTxnRef());
                    response = VnpIpnResponseConst.UNKNOWN_ERROR;
            }
        } catch (Exception e) {
            log.error("[VNPay Ipn] Unknown error occurred for txnRef: {}, error: {}", vnpayResponse.getVnpTxnRef(), e.getMessage(), e);
            response = VnpIpnResponseConst.UNKNOWN_ERROR;
        }
        log.info("[VNPay Ipn] txnRef: {}, response: {}", vnpayResponse.getVnpTxnRef(), response);
        return response;
    }

}
