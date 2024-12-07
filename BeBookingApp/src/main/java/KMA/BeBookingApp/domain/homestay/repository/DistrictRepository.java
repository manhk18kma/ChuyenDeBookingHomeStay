package KMA.BeBookingApp.domain.homestay.repository;

import KMA.BeBookingApp.domain.homestay.entity.Amenity;
import KMA.BeBookingApp.domain.homestay.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository  extends JpaRepository<District, Long> {
}
