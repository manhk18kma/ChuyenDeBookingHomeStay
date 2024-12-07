package KMA.BeBookingApp.domain.homestay.dto.response;

import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStatus;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStepStatus;
import KMA.BeBookingApp.domain.homestay.entity.HomestayAmenity;
import KMA.BeBookingApp.domain.homestay.entity.HomestaySpace;
import KMA.BeBookingApp.domain.homestay.entity.HomestayStyle;
import jakarta.persistence.*;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
public class HomestayConfirmationResponse {


    HomestayStyleResponse homestayStyle;

    String name;

    String description;

    Double   longitude;

    Double latitude;


    String addressDetail;

    String address;

    Integer maxGuests;


    List<AmenityResponse> amenities;

    List<SpaceResponse> spaces ;

    public HomestayConfirmationResponse(String name, String description, Double longitude, Double latitude, String addressDetail, String address, Integer maxGuests) {
        this.name = name;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.addressDetail = addressDetail;
        this.address = address;
        this.maxGuests = maxGuests;
    }
}
