package KMA.BeBookingApp.domain.user.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class FailedAttemptsResponse implements Serializable {
    private int remainingAttempts;

    public FailedAttemptsResponse(int remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
    }
}
