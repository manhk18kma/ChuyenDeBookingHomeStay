package KMA.BeBookingApp.domain.homestay.repository;

import KMA.BeBookingApp.domain.homestay.dto.response.HomestayStyleResponse;
import KMA.BeBookingApp.domain.homestay.entity.HomestayStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HomestayStyleRepository extends JpaRepository<HomestayStyle , Long> {


    @Query("SELECT hs FROM HomestayStyle hs")
    List<HomestayStyle> getAll();

    @Query("""
    SELECT new KMA.BeBookingApp.domain.homestay.dto.response.HomestayStyleResponse(
        hs.name, hs.description, hs.icon, hs.homestayStyleType
    )
    FROM Homestay h
    JOIN h.homestayStyle hs
    WHERE h.id = :homestayId
""")
    HomestayStyleResponse getHomestayStyleResponseByHomestayId(Long homestayId);


    @Query("""
    SELECT new KMA.BeBookingApp.domain.homestay.dto.response.HomestayStyleResponse(
        hs.id, hs.name, hs.description, hs.icon,null,null
    )
    FROM  HomestayStyle hs
""")
    List<HomestayStyleResponse> getAllToFilters();

    @Query("SELECT hs.id FROM HomestayStyle hs")
    List<Long> getAllIds();

}
