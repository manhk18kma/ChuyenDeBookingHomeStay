package KMA.BeBookingApp.domain.homestay.repository;

import KMA.BeBookingApp.domain.homestay.entity.Amenity;
import KMA.BeBookingApp.domain.homestay.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository  extends JpaRepository<Province, Long> {
}
