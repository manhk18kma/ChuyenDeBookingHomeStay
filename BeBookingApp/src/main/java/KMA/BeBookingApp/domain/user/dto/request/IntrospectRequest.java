package KMA.BeBookingApp.domain.user.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class IntrospectRequest implements Serializable {
    private String  token;
}
