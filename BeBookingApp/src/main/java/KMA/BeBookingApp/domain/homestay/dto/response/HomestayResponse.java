package KMA.BeBookingApp.domain.homestay.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
@Data
public class HomestayResponse implements Serializable {
    private Long id;
    private List<HomestayMediaResponse> medias; // Chỉnh kiểu dữ liệu nếu cần
    private String homestayStyleName;
    private String name;
    private String description;
    private Double longitude;
    private Double latitude;
    private String address;
    private Integer maxGuests;
    private Integer totalAmenities;
    private Integer totalSpaces;
    private BigDecimal avgVndPerNight;
    private BigDecimal avgUsdPerNight;
    private BigDecimal avgStars;

    public HomestayResponse(Long id, List<HomestayMediaResponse> medias, String homestayStyleName, String name, String description,
                            Double longitude, Double latitude, String address, Integer maxGuests, Integer totalAmenities,
                            Integer totalSpaces, Double avgVndPerNight, Double avgUsdPerNight, Double avgStars) {
        this.id = id;
        this.medias = medias;
        this.homestayStyleName = homestayStyleName;
        this.name = name;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.maxGuests = maxGuests;
        this.totalAmenities = totalAmenities;
        this.totalSpaces = totalSpaces;
        this.avgVndPerNight = BigDecimal.valueOf(avgVndPerNight);
        this.avgUsdPerNight = BigDecimal.valueOf(avgUsdPerNight);
        this.avgStars = BigDecimal.valueOf(avgStars);
    }

    // Getter và Setter nếu cần
}
