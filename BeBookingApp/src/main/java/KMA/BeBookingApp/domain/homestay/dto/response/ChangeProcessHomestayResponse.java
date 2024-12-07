package KMA.BeBookingApp.domain.homestay.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class ChangeProcessHomestayResponse implements Serializable {
    Long homestayId;
    String currentStep;
    String nextStep;
    String peakStep;

}
