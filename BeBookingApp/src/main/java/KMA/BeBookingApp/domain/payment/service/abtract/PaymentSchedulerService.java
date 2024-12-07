package KMA.BeBookingApp.domain.payment.service.abtract;

import org.springframework.scheduling.annotation.Scheduled;

public interface PaymentSchedulerService {
    @Scheduled(fixedRate = 30000)
    void cancelExpiredBookingsScheduled();
}
