package KMA.BeBookingApp.domain.booking.dto.request;

import KMA.BeBookingApp.domain.common.enumType.payment.PaymentMethod;
import KMA.BeBookingApp.domain.common.enumType.user.Gender;
import KMA.BeBookingApp.domain.common.validation.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public class BookingRequest implements Serializable {

    @NotNull(message = "homestay_id cannot be blank")
    private Long homestayId;

    @NotNull(message = "checkin_date cannot be blank")
    private LocalDate checkinDate;

    @NotNull(message = "checkout_date cannot be blank")
    private LocalDate checkoutDate;

    @Positive(message = "guests must be positive")
    private Integer guests;

    @Length(max = 500, message = "note cannot be longer than 500 characters")
    private String note;

    @NotBlank(message = "ipAddress cannot be blank")
    private String ipAddress;


    @EnumValue(name = "paymentMethod", enumClass = PaymentMethod.class, message = "paymentMethod phải là giá trị hợp lệ")
    private String paymentMethod;
}
