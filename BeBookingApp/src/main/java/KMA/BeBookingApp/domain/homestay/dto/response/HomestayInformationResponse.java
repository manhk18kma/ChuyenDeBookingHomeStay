package KMA.BeBookingApp.domain.homestay.dto.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HomestayInformationResponse {

    private String name;

    private String description;


    private Integer maxGuests;

    public HomestayInformationResponse(String name, String description, Integer maxGuests) {
        this.name = name;
        this.description = description;
        this.maxGuests = maxGuests;
    }
}
