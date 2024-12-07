package KMA.BeBookingApp.domain.homestay.repository;

import KMA.BeBookingApp.domain.homestay.dto.response.HomestayMediaResponse;
import KMA.BeBookingApp.domain.homestay.entity.Amenity;
import KMA.BeBookingApp.domain.homestay.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository  extends JpaRepository<Media, Long> {
    @Query("SELECT m FROM Media m WHERE m.homestay.id = :homestayId AND m.isPrimary = true")
    Optional<Media> findPrimaryMediaOfHomestay(Long homestayId);

    @Modifying
    @Query("DELETE FROM Media m WHERE m.homestay.id = :homestayId AND m.id IN :removedMediaIds")
    void removeInRemovedIds(Long homestayId, List<Long> removedMediaIds);

    @Query("SELECT COUNT(*) FROM Media m WHERE m.homestay.id = :homestayId AND m.isPrimary = false")
    long countRelatedMediaOfHomestay(@Param("homestayId") Long homestayId);


    @Query("SELECT m FROM Media m WHERE m.homestay.id = :homestayId")
    List<Media> getAllMediaOfHomestay(Long homestayId);

    @Query("SELECT m FROM Media m WHERE m.homestay.id = :homestayId and m.id IN :removedMediaIds")
    List<Media> findAllByIdAndHomestayId(@Param("removedMediaIds") List<Long> removedMediaIds,
                                         @Param("homestayId") Long homestayId);

    @Query("""
        SELECT new KMA.BeBookingApp.domain.homestay.dto.response.HomestayMediaResponse(
        m.id, m.mediaType, m.url, m.description, m.isPrimary
        )
        FROM Media m WHERE m.homestay.id = :homestayId
""")
    List<HomestayMediaResponse> getAllMediaOfHomestayCus(@Param("homestayId") Long homestayId);


}
