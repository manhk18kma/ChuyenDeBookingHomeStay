package KMA.BeBookingApp.domain.homestay.repository;

import KMA.BeBookingApp.domain.homestay.dto.response.AmenityResponse;
import KMA.BeBookingApp.domain.homestay.entity.Amenity;
import KMA.BeBookingApp.domain.homestay.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity , Long> {
    @Query("SELECT a FROM  Amenity a")
    List<Amenity> getAll();



    @Query("""
    SELECT new KMA.BeBookingApp.domain.homestay.dto.response.AmenityResponse(
                    a.id, 
                    a.description, 
                    a.icon, 
                    a.name, 
                    a.amenityType,
                    CASE WHEN ha.id IS NOT NULL THEN true ELSE false END
    )
    FROM Amenity a 
    LEFT JOIN HomestayAmenity ha ON ha.amenity = a AND ha.homestay.id = :homestayId
""")
    List<AmenityResponse> getAllCus(@Param("homestayId") Long homestayId);


    @Query("""
    SELECT new KMA.BeBookingApp.domain.homestay.dto.response.AmenityResponse(
                    a.id, 
                    a.description, 
                    a.icon, 
                    a.name, 
                    a.amenityType,
                    CASE WHEN ha.id IS NOT NULL THEN true ELSE false END
    )
    FROM Amenity a 
    JOIN HomestayAmenity ha ON ha.amenity = a AND ha.homestay.id = :homestayId
""")
    List<AmenityResponse> getAllAmenityOfHomestay(@Param("homestayId") Long homestayId);



    @Query("""
    SELECT new KMA.BeBookingApp.domain.homestay.dto.response.AmenityResponse(
                    a.id, 
                    a.description, 
                    a.icon, 
                    a.name,
                    null,
                    null
    )
    FROM Amenity a 
""")
    List<AmenityResponse> getAllToFilters();

    @Query("SELECT a.id FROM Amenity a")
    List<Long> getAllIds();
}
