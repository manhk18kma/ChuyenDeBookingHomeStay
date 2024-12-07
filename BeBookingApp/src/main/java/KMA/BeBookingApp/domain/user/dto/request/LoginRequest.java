package KMA.BeBookingApp.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class LoginRequest implements Serializable {
    @NotBlank(message = "Tên đăng nhập hoặc email không được để trống")
    private String usernameOrEmail;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @NotBlank(message = "TokenDeviceu không được để trống")
    private String tokenDevice;

    private String captcha;
}
