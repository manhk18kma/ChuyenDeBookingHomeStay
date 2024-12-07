package KMA.BeBookingApp.domain.homestay.entity;

import KMA.BeBookingApp.domain.common.AbstractEntity;
import KMA.BeBookingApp.domain.common.enumType.homestay.AmenityName;
import KMA.BeBookingApp.domain.common.enumType.homestay.AmenityType;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStyleName;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStyleType;
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
@Table(name = "homestay_style")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomestayStyle extends AbstractEntity<Long> {
    @Enumerated(EnumType.STRING)
    @Column(name = "homestay_style_name")
    HomestayStyleName homestayStyleName;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "icon")
    String icon;

    @Column(name = "name")
    String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "homestay_style_type")
    HomestayStyleType homestayStyleType;
}
