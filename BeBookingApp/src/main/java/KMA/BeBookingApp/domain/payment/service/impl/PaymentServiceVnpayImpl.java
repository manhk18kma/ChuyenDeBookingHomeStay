package KMA.BeBookingApp.domain.payment.service.impl;

import KMA.BeBookingApp.domain.common.dto.request.InitPaymentVnPayRequest;
import KMA.BeBookingApp.domain.common.enumType.payment.PaymentMethod;
import KMA.BeBookingApp.domain.common.enumType.payment.PaymentStatus;
import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import KMA.BeBookingApp.domain.common.exception.AppException;
import KMA.BeBookingApp.domain.payment.dto.request.InitDepositPayRequest;
import KMA.BeBookingApp.domain.common.dto.response.InitDepositResponse;
import KMA.BeBookingApp.domain.payment.entity.Payment;
import KMA.BeBookingApp.domain.payment.repository.PaymentRepository;
import KMA.BeBookingApp.domain.payment.service.abtract.PaymentService;
import KMA.BeBookingApp.domain.payment.service.vnpay.VnpayService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceVnpayImpl implements PaymentService {
    PaymentRepository paymentRepository;
    VnpayService vnpayService;

    @Override
    public InitDepositResponse initPayment(InitPaymentVnPayRequest request) {
        Payment payment = Payment.builder()
                .amountVND(request.getAmountVND())
                .status(PaymentStatus.PENDING)
                .paymentMethod(PaymentMethod.VNPAY)
                .bookingId(request.getBookingId())
                .expiredAt(LocalDateTime.now().plus(10, ChronoUnit.MINUTES))
                .build();
        Payment paymentSaved = paymentRepository.save(payment);

        InitDepositPayRequest initDepositPayRequest = InitDepositPayRequest.builder()
                .amountVND(request.getAmountVND())
                .ipAddress(request.getIpAddress())
                .txnRef(paymentSaved.getId())
                .build();

        InitDepositResponse response =  vnpayService.init(initDepositPayRequest);
        response.setExpiredTransaction(paymentSaved.getExpiredAt());
        return response;
    }

    @Override
    public Long markPaymentSuccess(Long paymentId) {
        Payment payment = paymentRepository.findPaymentForHandleCallback(paymentId).orElseThrow(
                ()->new AppException(AppErrorCode.BOOKING_NOT_FOUND)
        );
        payment.setStatus(PaymentStatus.SUCCESS);
        return paymentRepository.save(payment).getBookingId();
    }

    @Override
    public void cancelPayment(Long bookingId) {
        paymentRepository.cancelPayment(bookingId);
    }


}
