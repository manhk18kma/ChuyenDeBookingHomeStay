package KMA.BeBookingApp.domain.homestay.entity;

import KMA.BeBookingApp.domain.common.AbstractEntity;
import KMA.BeBookingApp.domain.common.enumType.homestay.AmenityName;
import KMA.BeBookingApp.domain.common.enumType.homestay.AmenityType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "amenities")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Amenity extends AbstractEntity<Long> {

    @Enumerated(EnumType.STRING)
    @Column(name = "amenity_name" , length = 100)
    AmenityName amenityName;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "icon")
    String icon;

    @Column(name = "name")
    String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "amenity")
    Set<HomestayAmenity> homestays = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "amenity_type" , length = 100)
    AmenityType amenityType;
}
