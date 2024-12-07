package KMA.BeBookingApp.domain.homestay.dto.response;

import KMA.BeBookingApp.domain.common.enumType.homestay.MediaType;
import KMA.BeBookingApp.domain.homestay.entity.Homestay;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HomestayMediaResponse {
    private Long id;

    private MediaType mediaType;

    private String url;

    private String description;

    private Boolean isPrimary;

    public HomestayMediaResponse(Long id, MediaType mediaType, String url, String description, Boolean isPrimary) {
        this.id = id;
        this.mediaType = mediaType;
        this.url = url;
        this.description = description;
        this.isPrimary = isPrimary;
    }

    public String checkPrimary() {
        return isPrimary ? "Primary" : "Related";
    }




}
