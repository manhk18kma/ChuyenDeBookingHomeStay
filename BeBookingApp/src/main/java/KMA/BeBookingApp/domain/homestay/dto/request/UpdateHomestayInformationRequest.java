package KMA.BeBookingApp.domain.homestay.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UpdateHomestayInformationRequest implements Serializable {

    @NotNull(message = "Tên homestay không được trống.")
    @Size(min = 3, max = 100, message = "Tên homestay phải có độ dài từ 3 đến 100 ký tự.")
    private String name;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự.")
    private String description;

    @NotNull(message = "Số khách tối đa không được trống.")
    @Min(value = 1, message = "Số khách tối đa phải lớn hơn hoặc bằng 1.")
    @Max(value = 100, message = "Số khách tối đa không được vượt quá 100.")
    private Integer maxGuests;
}
