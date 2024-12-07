package KMA.BeBookingApp.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ChangePasswordRequest implements Serializable {

    @NotBlank(message = "Token không được để trống")
    private String token;

    private String password;

    private String confirmPassword;

    @NotBlank(message = "TokenDevice không được để trống")
    private String tokenDevice;


}
