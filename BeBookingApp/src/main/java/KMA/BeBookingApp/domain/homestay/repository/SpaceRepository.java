package KMA.BeBookingApp.domain.homestay.repository;

import KMA.BeBookingApp.domain.homestay.dto.response.AmenityResponse;
import KMA.BeBookingApp.domain.homestay.dto.response.SpaceResponse;
import KMA.BeBookingApp.domain.homestay.entity.Amenity;
import KMA.BeBookingApp.domain.homestay.entity.HomestaySpace;
import KMA.BeBookingApp.domain.homestay.entity.HomestayStyle;
import KMA.BeBookingApp.domain.homestay.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpaceRepository  extends JpaRepository<Space, Long> {
    @Query("SELECT s FROM Space s ")
    List<Space> getAll();

    @Query("""
    SELECT new KMA.BeBookingApp.domain.homestay.dto.response.SpaceResponse(
                    s.id, 
                    s.description, 
                    s.icon, 
                    s.name, 
                    s.spaceType, 
                    CASE WHEN hp.id IS NOT NULL THEN true ELSE false END
    )
    FROM Space s 
    LEFT JOIN HomestaySpace hp ON hp.space.id = s.id AND hp.homestay.id = :homestayId
""")
    List<SpaceResponse> getAllCus(@Param("homestayId") Long homestayId);



    @Query("""
    SELECT new KMA.BeBookingApp.domain.homestay.dto.response.SpaceResponse(
                    s.id, 
                    s.description, 
                    s.icon, 
                    s.name, 
                    s.spaceType, 
                    CASE WHEN hp.id IS NOT NULL THEN true ELSE false END
    )
    FROM Space s 
     JOIN HomestaySpace hp ON hp.space.id = s.id AND hp.homestay.id = :homestayId
""")
    List<SpaceResponse> getAllSpaceOfHomestay(@Param("homestayId") Long homestayId);


    @Query("""
    SELECT new KMA.BeBookingApp.domain.homestay.dto.response.SpaceResponse(
                    s.id, 
                    s.description, 
                    s.icon, 
                    s.name,
                    null,
                    null
    )
    FROM Space s 
""")
    List<SpaceResponse> getAllToFilters();



    @Query("SELECT sp.id FROM Space sp")
    List<Long> getAllIds();
}
