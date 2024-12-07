package KMA.BeBookingApp.domain.user.dto.request;

import KMA.BeBookingApp.domain.common.enumType.user.Gender;
import KMA.BeBookingApp.domain.common.enumType.user.Platform;
import KMA.BeBookingApp.domain.common.validation.EnumValue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OutBoundLoginRequest implements Serializable {

//    @NotEmpty(message = "Token device không được để trống")
    private String tokenDevice;

    private String idToken;

    @EnumValue(name = "platform", enumClass = Platform.class, message = "platform phải là giá trị hợp lệ")
    private String platform;

    private String deviceId;

}
