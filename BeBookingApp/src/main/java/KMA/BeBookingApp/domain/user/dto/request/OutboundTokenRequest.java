package KMA.BeBookingApp.domain.user.dto.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class OutboundTokenRequest implements Serializable {
    private String  code;
}
