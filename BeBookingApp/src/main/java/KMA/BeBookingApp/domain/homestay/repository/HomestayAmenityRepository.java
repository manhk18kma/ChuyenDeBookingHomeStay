package KMA.BeBookingApp.domain.homestay.repository;

import KMA.BeBookingApp.domain.homestay.entity.Amenity;
import KMA.BeBookingApp.domain.homestay.entity.HomestayAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HomestayAmenityRepository extends JpaRepository<HomestayAmenity, Long> {

    // Lấy danh sách các tiện nghi hiện tại của homestay
    @Query("SELECT ha.amenity.id FROM HomestayAmenity ha WHERE ha.homestay.id = :homestayId")
    List<Long> getCurrentAmenityOfHomestay(Long homestayId);

    // Xóa các tiện nghi khỏi homestay
    @Modifying
    @Query("DELETE FROM HomestayAmenity ha WHERE ha.homestay.id = :homestayId AND ha.amenity.id IN :removedAmenityId")
    void removeAmenitiesFromHomestay(Long homestayId, List<Long> removedAmenityId);
}
