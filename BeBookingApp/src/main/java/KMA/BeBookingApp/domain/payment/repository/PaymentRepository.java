package KMA.BeBookingApp.domain.payment.repository;

import KMA.BeBookingApp.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment , Long> {
    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING' and p.id = :paymentId")
    Optional<Payment> findPaymentForHandleCallback(Long paymentId);


    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING' and p.expiredAt < :now")
    List<Payment> findExpiredPayment(@Param("now") LocalDateTime now);



    @Modifying
    @Query("""
    UPDATE Payment p 
    SET p.status = 'FAILED'
    WHERE 
        p.bookingId = :bookingId
        AND p.status = 'PENDING'
""")
    int cancelPayment(Long bookingId);

}
