package KMA.BeBookingApp.domain.homestay.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class HomestayCardResponse {
    private Long id;
    private String name;
    private String primaryImage;
    private LocalDateTime updateAd;
    private String descriptionProcess;
    private String typeRedirect;
    private String address;

}
