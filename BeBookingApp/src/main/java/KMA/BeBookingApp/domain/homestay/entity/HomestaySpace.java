package KMA.BeBookingApp.domain.homestay.entity;

import KMA.BeBookingApp.domain.common.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "homestay_space")
public class HomestaySpace extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homestay_id")
    private Homestay homestay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

//    @Column(name = "is_available")
//    Boolean isAvailable;
//
//    @Column(name = "additional_cost", columnDefinition = "DOUBLE PRECISION")
//    Double additionalCost;
//
//    @Column(name = "quantity")
//    Integer quantity;
//
//    @Column(name = "area_size", columnDefinition = "DOUBLE PRECISION")
//    Double areaSize;

}
