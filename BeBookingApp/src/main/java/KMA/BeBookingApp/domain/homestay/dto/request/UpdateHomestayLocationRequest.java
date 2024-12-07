package KMA.BeBookingApp.domain.homestay.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UpdateHomestayLocationRequest implements Serializable {

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(min = 5, max = 255, message = "Địa chỉ phải có độ dài từ 5 đến 255 ký tự")
    private String address;

    private String addressDetail;

    @NotNull(message = "Kinh độ không được để trống")
    @DecimalMin(value = "-180.0", message = "Kinh độ phải nằm trong khoảng từ -180 đến 180")
    @DecimalMax(value = "180.0", message = "Kinh độ phải nằm trong khoảng từ -180 đến 180")
    private Double longitude;

    @NotNull(message = "Vĩ độ không được để trống")
    @DecimalMin(value = "-90.0", message = "Vĩ độ phải nằm trong khoảng từ -90 đến 90")
    @DecimalMax(value = "90.0", message = "Vĩ độ phải nằm trong khoảng từ -90 đến 90")
    private Double latitude;

}
