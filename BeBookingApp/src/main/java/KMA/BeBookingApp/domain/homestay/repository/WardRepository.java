package KMA.BeBookingApp.domain.homestay.repository;

import KMA.BeBookingApp.domain.homestay.entity.Amenity;
import KMA.BeBookingApp.domain.homestay.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WardRepository  extends JpaRepository<Ward, Long> {
}
