package KMA.BeBookingApp.domain.user.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class TokenResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
}
