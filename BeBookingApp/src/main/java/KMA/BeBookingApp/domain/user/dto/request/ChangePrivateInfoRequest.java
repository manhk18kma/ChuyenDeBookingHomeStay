package KMA.BeBookingApp.domain.user.dto.request;

import KMA.BeBookingApp.domain.common.enumType.user.Gender;
import KMA.BeBookingApp.domain.common.validation.DateAnnotationCus;
import KMA.BeBookingApp.domain.common.validation.EmailValidation;
import KMA.BeBookingApp.domain.common.validation.EnumValue;
import KMA.BeBookingApp.domain.common.validation.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.io.Serializable;

@Getter
@Setter
public class ChangePrivateInfoRequest implements Serializable {

    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 20, message = "Username phải có ít nhất 3 ký tự và tối đa 20 ký tự")
    private String username;

    @NotBlank(message = "Tên không được để trống")
    private String fullName;

    @DateAnnotationCus(format = "yyyy-MM-dd", message = "Ngày sinh phải có định dạng yyyy-MM-dd và nằm trong khoảng từ năm 1930 đến hiện tại")
    private String dateOfBirth;

    @EnumValue(name = "Giới tính", enumClass = Gender.class, message = "Giới tính phải là giá trị hợp lệ")
    private String gender;

    @PhoneNumber(message = "Số điện thoại phải đúng định dạng")
    private String phone;
}
