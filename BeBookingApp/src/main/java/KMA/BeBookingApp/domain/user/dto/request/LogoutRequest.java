package KMA.BeBookingApp.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class LogoutRequest implements Serializable {

    @NotBlank(message = "Refresh token không được để trống")
    private String refreshToken;

    @NotBlank(message = "Access token không được để trống")
    private String accessToken;

}
