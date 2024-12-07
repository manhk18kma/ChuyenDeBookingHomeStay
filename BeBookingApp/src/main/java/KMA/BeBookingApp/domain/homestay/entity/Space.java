package KMA.BeBookingApp.domain.homestay.entity;

import KMA.BeBookingApp.domain.common.AbstractEntity;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStyleName;
import KMA.BeBookingApp.domain.common.enumType.homestay.SpaceName;
import KMA.BeBookingApp.domain.common.enumType.homestay.SpaceType;
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
@Table(name = "spaces")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Space extends AbstractEntity<Long> {
    @Enumerated(EnumType.STRING)
    @Column(name = "space_name")
    SpaceName spaceName;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "icon")
    String icon;

    @Column(name = "name")
    String name;


    @Column(name = "space_type" ,  length = 100)
    @Enumerated(EnumType.STRING)
    SpaceType spaceType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "space")
    private Set<HomestaySpace> homestays = new HashSet<>();

}
