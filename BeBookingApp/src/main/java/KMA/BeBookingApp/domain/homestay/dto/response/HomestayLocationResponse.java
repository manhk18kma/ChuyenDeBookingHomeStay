package KMA.BeBookingApp.domain.homestay.dto.response;

import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStepStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import org.locationtech.jts.geom.Point;
@Data
@Builder

public class HomestayLocationResponse {
    Double longitude;
    Double latitude;
    String addressDetail;
    String address;

    public HomestayLocationResponse(Double longitude, Double latitude, String addressDetail, String address) {
        this.longitude = longitude;
        this.latitude = latitude;

        this.addressDetail = addressDetail;
        this.address = address;
    }
}
