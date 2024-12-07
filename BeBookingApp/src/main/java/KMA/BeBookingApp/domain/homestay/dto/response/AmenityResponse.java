package KMA.BeBookingApp.domain.homestay.dto.response;

import KMA.BeBookingApp.domain.common.enumType.homestay.AmenityName;
import KMA.BeBookingApp.domain.common.enumType.homestay.AmenityType;
import KMA.BeBookingApp.domain.homestay.entity.Amenity;
import KMA.BeBookingApp.domain.homestay.entity.HomestayAmenity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class AmenityResponse {
    Long id;

    String description;

    String icon;

    String name;

    AmenityType amenityType;

    Boolean isSelected;

    public AmenityResponse(Long id, String description, String icon, String name, AmenityType amenityType, Boolean isSelected) {
        this.id = id;
        this.description = description;
        this.icon = icon;
        this.name = name;
        this.amenityType = amenityType;
        this.isSelected = isSelected;
    }



    public String getAmenityType() {
        return amenityType != null ? amenityType.getName().toString() : "Unknown";
    }
}
