package KMA.BeBookingApp.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class InitUsernameRequest implements Serializable {

    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 20, message = "Username phải có ít nhất 3 ký tự và tối đa 20 ký tự")
    private String username;

}
