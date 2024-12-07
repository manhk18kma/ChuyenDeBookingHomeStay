package KMA.BeBookingApp.domain.homestay.repository;

import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStatus;
import KMA.BeBookingApp.domain.homestay.dto.response.*;
import KMA.BeBookingApp.domain.homestay.entity.Homestay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HomestayRepository  extends JpaRepository<Homestay, Long> {

    @Query("""
    SELECT h FROM Homestay h 
    WHERE h.hostId = :hostId 
        AND h.homestayStatus != 'REMOVED' 
        AND (:homestayStatus IS NULL OR h.homestayStatus = :homestayStatus)
    ORDER BY h.updatedAt DESC
""")
    List<Homestay> getAllHomestayByHostId(@Param("hostId") Long hostId,
                                          @Param("homestayStatus") HomestayStatus homestayStatus);


    @Query("""
    SELECT new KMA.BeBookingApp.domain.homestay.dto.response.HomestayLocationResponse(
        h.longitude , h.latitude  , h.addressDetail , h.address
    )
    FROM Homestay h WHERE h.id = :homestayId and h.homestayStatus != 'REMOVED'
""")
    HomestayLocationResponse getHomestayLocation(Long homestayId);

    @Query("""
    SELECT new KMA.BeBookingApp.domain.homestay.dto.response.HomestayInformationResponse(
        h.name , h.description  , h.maxGuests 
    )
    FROM Homestay h WHERE h.id = :homestayId and h.homestayStatus != 'REMOVED'
""")
    HomestayInformationResponse getHomestayInformation(Long homestayId);


    @Query("""
    SELECT new KMA.BeBookingApp.domain.homestay.dto.response.HomestayConfirmationResponse(
        h.name, h.description, h.longitude, h.latitude, h.addressDetail, h.address, h.maxGuests
    )
    FROM Homestay h
    WHERE h.id = :homestayId and h.homestayStatus != 'REMOVED'
""")
    HomestayConfirmationResponse getHomestayConfirmation(Long homestayId);


    @Query("SELECT h FROM Homestay h WHERE h.id = :homestayId and h.homestayStatus != 'REMOVED'")
    Optional<Homestay> findByIdCus(Long homestayId);


    @Query("""
    SELECT new KMA.BeBookingApp.domain.homestay.dto.response.HomestayDetailOwnerResponse(
        h.name, h.description, h.longitude, h.latitude, h.addressDetail, h.address, h.maxGuests
    )
    FROM Homestay h
    WHERE h.id = :homestayId and h.homestayStatus != 'REMOVED'
""")
    Optional<HomestayDetailOwnerResponse> getHomestayDetailOwner(Long homestayId);

    @Modifying
    @Query("UPDATE Homestay h SET h.geom = CAST(ST_Transform(ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), 3857) AS org.locationtech.jts.geom.Point) WHERE h.id = :id")
    void saveLocation(@Param("id") Long id, @Param("longitude") Double longitude, @Param("latitude") Double latitude);

    @Query("SELECT h FROM Homestay h WHERE h.id = :homestayId and h.homestayStatus = 'AVAILABLE'")
    Optional<Homestay> findByIdToBook(Long homestayId);





//    @Query(value = """
//    WITH destination AS (
//        SELECT
//            st_transform(st_setsrid(st_makepoint(105.74783258465845, 20.955933368235403), 4326), 3857) AS geom
//    )
//    SELECT
//        hs.id
//    FROM
//        homestays hs
//    JOIN
//        destination d ON st_dwithin(hs.geom, d.geom, 25000)
//    INNER JOIN (
//        SELECT
//            h.id
//        FROM
//            homestays h
//        JOIN
//            homestay_availability ha ON h.id = ha.homestay_id
//        LEFT JOIN
//            homestay_space h_sp ON h.id = h_sp.homestay_id
//        LEFT JOIN
//            homestay_amenities h_am ON h.id = h_am.homestay_id
//        WHERE
//            (200000 IS NULL OR ha.pricevnd >= 100000)
//            AND (500000 IS NULL OR ha.pricevnd <= 500000)
//            AND (ARRAY[5, 7] IS NULL OR h_sp.space_id IN (1,2,3,4,5,6,7,11,12))
//            AND (ARRAY[4, 5] IS NULL OR h_am.amenity_id IN (1,2,3,4,5,6,7,11,12))
//            AND (3 IS NULL OR h.homestay_style_id = 3)
//            AND (2 IS NULL OR h.max_guests >= 1)
//            AND ('2024-11-27' IS NULL OR '2024-12-01' IS NULL
//                 OR ha.date BETWEEN '2024-11-27' AND '2024-12-03')
//            AND ha.homestay_availability_status = 'AVAILABLE'
//        GROUP BY
//            h.id
//        HAVING
//            COUNT(ha.date) >= 3
//    ) AS vh ON hs.id = vh.id
//    ORDER BY
//        hs.geom <-> d.geom
//""", nativeQuery = true)
//    List<Long> searchWithLocation(Double latitude, Double longitude, Long minPriceVnd, Long maxPriceVnd, List<Long> spaceIds, List<Long> amenityIds, Long styleId, Integer guests, LocalDate checkinDate, LocalDate checkoutDate, Integer radius, Integer nights);
//;



    @Query(value = """
WITH destination AS (
    SELECT
        st_transform(
            st_setsrid(st_makepoint(:longitude, :latitude), 4326), 3857
        ) AS geom
)
SELECT
    hs.id
FROM
    homestays hs
JOIN
    destination d
    ON st_dwithin(hs.geom, d.geom, :radius)
INNER JOIN (
    SELECT
        h.id
    FROM
        homestays h
    JOIN
        homestay_availability ha
        ON h.id = ha.homestay_id
    LEFT JOIN
        homestay_space h_sp
        ON h.id = h_sp.homestay_id
    LEFT JOIN
        homestay_amenities h_am
        ON h.id = h_am.homestay_id
    WHERE
        ha.pricevnd >= :minPriceVnd
        AND ha.pricevnd <= :maxPriceVnd
        AND h.homestay_status = 'AVAILABLE'
        AND h.homestay_style_id IN :styleIds
        AND h.max_guests >= :guests
        AND h_sp.space_id IN :spaceIds
        AND h_am.amenity_id IN :amenityIds
        AND ha.date BETWEEN :checkinDate AND :checkoutDate
        AND ha.homestay_availability_status = 'AVAILABLE'
    GROUP BY
        h.id
    HAVING
        COUNT(distinct ha.date) >= :nights
) AS vh
ON hs.id = vh.id
ORDER BY
    hs.geom <-> d.geom
""", nativeQuery = true)
    List<Long> searchWithLocation(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("minPriceVnd") Long minPriceVnd,
            @Param("maxPriceVnd") Long maxPriceVnd,
            @Param("styleIds") List<Long> styleId,
            @Param("spaceIds") List<Long> spaceIds,
            @Param("amenityIds") List<Long> amenityIds,
            @Param("guests") Integer guests,
            @Param("checkinDate") LocalDate checkinDate,
            @Param("checkoutDate") LocalDate checkoutDate,
            @Param("radius") Integer radius,
            @Param("nights") Integer nights);






    @Query(value = """
 SELECT
        h.id
    FROM
        homestays h
    JOIN
        homestay_availability ha
        ON h.id = ha.homestay_id
    LEFT JOIN
        homestay_space h_sp
        ON h.id = h_sp.homestay_id
    LEFT JOIN
        homestay_amenities h_am
        ON h.id = h_am.homestay_id
    WHERE
        ha.pricevnd >= :minPriceVnd
        AND ha.pricevnd <= :maxPriceVnd
        AND h.homestay_status = 'AVAILABLE'
        AND h.homestay_style_id IN :styleIds
        AND h.max_guests >= :guests
        AND h_sp.space_id IN :spaceIds
        AND h_am.amenity_id IN :amenityIds
        AND ha.date BETWEEN :checkinDate AND :checkoutDate
        AND ha.homestay_availability_status = 'AVAILABLE'
    GROUP BY
        h.id
    HAVING
        COUNT(distinct ha.date) >= :nights""" , nativeQuery = true)
    List<Long> searchWithoutLocation(
            @Param("minPriceVnd") Long minPriceVnd,
            @Param("maxPriceVnd") Long maxPriceVnd,
            @Param("styleIds") List<Long> styleId,
            @Param("spaceIds") List<Long> spaceIds,
            @Param("amenityIds") List<Long> amenityIds,
            @Param("guests") Integer guests,
            @Param("checkinDate") LocalDate checkinDate,
            @Param("checkoutDate") LocalDate checkoutDate,
            @Param("nights") Integer nights);







    @Query("""
        SELECT new KMA.BeBookingApp.domain.homestay.dto.response.HomestayResponse(
            h.id,
            null, 
            h.homestayStyle.name,
            h.name,
            h.description,
            h.longitude,
            h.latitude,
            h.address,
            h.maxGuests,
            SIZE(h.amenities),
            SIZE(h.spaces),
            10.0,
            10.0,
            10.0
        )
        FROM Homestay h
        WHERE h.id IN :homestayIds
    """)
    List<HomestayResponse> buildSearchHomestayResponse(@Param("homestayIds") List<Long> homestayIds);



//    JPQL ----------------------------------------------------------


//    @Query("""
//    SELECT hs.id
//    FROM Homestay hs
//    JOIN HomestayAvailability ha on hs.id = ha.homestayId
//    LEFT JOIN hs.spaces hSp
//    LEFT JOIN hs.amenities hAm
//    WHERE
//        (6371000 * ACOS(
//            COS(RADIANS(:latitude)) * COS(RADIANS(hs.latitude)) * COS(RADIANS(hs.longitude) - RADIANS(:longitude)) +
//            SIN(RADIANS(:latitude)) * SIN(RADIANS(hs.latitude))
//        )) <= :radius
//        AND ha.priceVND BETWEEN :minPriceVnd AND :maxPriceVnd
//        AND hs.homestayStatus = 'AVAILABLE'
//        AND hs.homestayStyle.id IN :styleIds
//        AND hs.maxGuests >= :guests
//        AND hSp.space.id IN :spaceIds
//        AND hAm.amenity.id IN :amenityIds
//        AND ha.date BETWEEN :checkinDate AND :checkoutDate
//        AND ha.homestayAvailabilityStatus = 'AVAILABLE'
//    GROUP BY hs.id
//    HAVING COUNT(DISTINCT ha.date) >= :nights
//    ORDER BY (6371000 * ACOS(
//            COS(RADIANS(:latitude)) * COS(RADIANS(hs.latitude)) * COS(RADIANS(hs.longitude) - RADIANS(:longitude)) +
//            SIN(RADIANS(:latitude)) * SIN(RADIANS(hs.latitude))
//        ))
//""")
//    List<Long> searchWithLocationJPQL(
//            @Param("latitude") Double latitude,
//            @Param("longitude") Double longitude,
//            @Param("minPriceVnd") Long minPriceVnd,
//            @Param("maxPriceVnd") Long maxPriceVnd,
//            @Param("styleIds") List<Long> styleIds,
//            @Param("spaceIds") List<Long> spaceIds,
//            @Param("amenityIds") List<Long> amenityIds,
//            @Param("guests") Integer guests,
//            @Param("checkinDate") LocalDate checkinDate,
//            @Param("checkoutDate") LocalDate checkoutDate,
//            @Param("radius") Integer radius,
//            @Param("nights") Integer nights
//    );


}







//JPQL

