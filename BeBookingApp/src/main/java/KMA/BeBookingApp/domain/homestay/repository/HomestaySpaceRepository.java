package KMA.BeBookingApp.domain.homestay.repository;

import KMA.BeBookingApp.domain.homestay.entity.Amenity;
import KMA.BeBookingApp.domain.homestay.entity.HomestaySpace;
import KMA.BeBookingApp.domain.homestay.entity.HomestayStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomestaySpaceRepository  extends JpaRepository<HomestaySpace, Long> {

    @Query("SELECT hs.space.id FROM HomestaySpace hs WHERE hs.homestay.id = :homestayId")
    List<Long> getCurrentSpaceOfHomestay(@Param("homestayId") Long homestayId);

    @Modifying
    @Query("DELETE FROM HomestaySpace hs WHERE hs.homestay.id = :homestayId AND hs.space.id IN :removedSpaceId")
    void removeSpacesFromHomestay(@Param("homestayId") Long homestayId, @Param("removedSpaceId") List<Long> removedSpaceId);

}
