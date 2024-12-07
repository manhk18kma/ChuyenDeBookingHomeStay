package KMA.BeBookingApp.domain.payment.service.impl;

import KMA.BeBookingApp.domain.booking.service.abtract.BookingService;
import KMA.BeBookingApp.domain.common.enumType.payment.PaymentStatus;
import KMA.BeBookingApp.domain.payment.entity.Payment;
import KMA.BeBookingApp.domain.payment.repository.PaymentRepository;
import KMA.BeBookingApp.domain.payment.service.abtract.PaymentSchedulerService;
import KMA.BeBookingApp.domain.payment.service.abtract.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class PaymentSchedulerServiceImpl implements PaymentSchedulerService {

    BookingService bookingService;
    PaymentRepository paymentRepository;
    @Override
    @Scheduled(fixedRate = 300000)
    @Transactional
    public void cancelExpiredBookingsScheduled() {
        List<Payment> expiredPayments = paymentRepository.findExpiredPayment(LocalDateTime.now());
        expiredPayments.stream().forEach(payment -> {
            payment.setStatus(PaymentStatus.FAILED);
            bookingService.cancelCronJobBooking(payment.getBookingId());
        });
        paymentRepository.saveAll(expiredPayments);
    }
}
