package KMA.BeBookingApp.domain.booking.dto.response;
import KMA.BeBookingApp.domain.common.enumType.booking.BookingStatus;
import KMA.BeBookingApp.domain.common.enumType.payment.PaymentMethod;
import KMA.BeBookingApp.domain.common.enumType.payment.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Builder
@Data
public class BookingResponse implements Serializable{
    private Long bookingId;
    private Long homestayId;
    LocalDateTime expiredTransaction;
    String urlPayment;
}
