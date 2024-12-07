package KMA.BeBookingApp.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class IntrospectResponse implements Serializable {
    private Boolean valid;
}
