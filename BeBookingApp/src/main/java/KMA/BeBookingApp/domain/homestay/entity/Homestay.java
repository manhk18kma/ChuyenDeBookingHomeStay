package KMA.BeBookingApp.domain.homestay.entity;

import KMA.BeBookingApp.domain.common.AbstractEntity;
import KMA.BeBookingApp.domain.common.enumType.homestay.AddressType;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStatus;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStepStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "homestays")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Homestay extends AbstractEntity<Long> {

    @Column(name = "host_id")
    Long hostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homestay_style_id", referencedColumnName = "id")
    HomestayStyle homestayStyle;

    @Column(name = "name")
    String name;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "homestay_status")
    HomestayStatus homestayStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "homestay_step_status")
    HomestayStepStatus homestayStepStatus;

    @Column(name = "longitude", columnDefinition = "DOUBLE PRECISION")
    Double longitude;

    @Column(name = "latitude", columnDefinition = "DOUBLE PRECISION")
    Double latitude;

    @Column(name = "geom", columnDefinition = "Geometry(Point, 3857)")
    Point geom;

//    @Column(name = "geom", columnDefinition = "Geometry(Point, 4326)")
//    Point geom;



    @Column(name = "address_detail")
    String addressDetail;

    @Column(name = "address")
    String address;

    @Column(name = "max_guests")
    Integer maxGuests;



//    @Enumerated(EnumType.STRING)
//    @Column(name = "address_type" , length = 100)
//    AddressType addressType;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ward_id", referencedColumnName = "id")
//    Ward ward;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "district_id", referencedColumnName = "id")
//    District district;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "province_id", referencedColumnName = "id")
//    Province province;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "homestay")
    Set<HomestayAmenity> amenities = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "homestay")
    Set<HomestaySpace> spaces = new HashSet<>();

//     Boolean isRemoved = false;
}

//quá trình tạo 1 homestay :information -> style -> space -> amenity -> images -> location -> xác nhận