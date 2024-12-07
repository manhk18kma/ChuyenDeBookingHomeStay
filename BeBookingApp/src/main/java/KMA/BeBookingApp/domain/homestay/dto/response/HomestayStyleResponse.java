package KMA.BeBookingApp.domain.homestay.dto.response;

import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStyleType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Data
public class HomestayStyleResponse {
    private Long id;
    private String name;
    private String description;
    private String icon;
    private String homestayStyleType;
    private Boolean isSelected;


    public HomestayStyleResponse(String name, String description, String icon, HomestayStyleType homestayStyleType) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.homestayStyleType = homestayStyleType.getTypeName();
    }

    public HomestayStyleResponse(Long id, String name, String description, String icon) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public HomestayStyleResponse(Long id, String name, String description, String icon, String homestayStyleType, Boolean isSelected) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.homestayStyleType = homestayStyleType;
        this.isSelected = isSelected;
    }
}
