package KMA.BeBookingApp.domain.homestay.dto.response;

import KMA.BeBookingApp.domain.common.enumType.homestay.SpaceName;
import KMA.BeBookingApp.domain.common.enumType.homestay.SpaceType;
import KMA.BeBookingApp.domain.homestay.entity.HomestaySpace;
import KMA.BeBookingApp.domain.homestay.entity.Space;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
public class SpaceResponse {
     Long id;

    String description;

    String icon;

    String name;

    SpaceType spaceType;

    Boolean isSelected;


    public SpaceResponse(Long id, String description, String icon, String name, SpaceType spaceType, Boolean isSelected) {
        this.id = id;
        this.description = description;
        this.icon = icon;
        this.name = name;
        this.spaceType = spaceType;
        this.isSelected = isSelected;
    }

    public String getSpaceType() {
        return spaceType != null ? spaceType.getName().toString() : "Unknown";
    }
}
