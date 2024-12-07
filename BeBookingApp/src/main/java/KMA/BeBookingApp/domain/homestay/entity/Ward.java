package KMA.BeBookingApp.domain.homestay.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wards")
public class Ward {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "full_name_en")
    private String fullNameEn;

    @Column(name = "longitude", columnDefinition = "DOUBLE PRECISION")
    private Double longitude; // Kinh độ

    @Column(name = "latitude", columnDefinition = "DOUBLE PRECISION")
    private Double latitude; // Vĩ độ

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

}
