package KMA.BeBookingApp.domain.homestay.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HomestayDetailOwnerResponse implements Serializable {
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

    public HomestayDetailOwnerResponse(String name, String description, Double longitude, Double latitude, String addressDetail, String address, Integer maxGuests) {
        this.name = name;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.addressDetail = addressDetail;
        this.address = address;
        this.maxGuests = maxGuests;
    }
}
