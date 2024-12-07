package KMA.BeBookingApp.domain.booking.repository;

import KMA.BeBookingApp.domain.booking.dto.response.HomestayAvailabilityResponse;
import KMA.BeBookingApp.domain.booking.entity.HomestayAvailability;
import KMA.BeBookingApp.domain.common.enumType.booking.HomestayAvailabilityStatus;
import KMA.BeBookingApp.domain.homestay.dto.response.AvgPriceResponse;
import KMA.BeBookingApp.domain.homestay.dto.response.PriceRangeResponse;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HomestayAvailabilityRepository extends JpaRepository<HomestayAvailability, Long> {

    @Modifying
    @Query("""
    UPDATE HomestayAvailability ha
    SET ha.homestayAvailabilityStatus = 
        CASE 
            WHEN ha.date = :yesterday AND ha.homestayAvailabilityStatus NOT IN ('BOOKED', 'PENDING') THEN 'UNAVAILABLE'
            ELSE ha.homestayAvailabilityStatus
        END,
        ha.rate = 
        CASE
            WHEN ha.date >= :today AND ha.homestayAvailabilityStatus NOT IN ('BOOKED', 'PENDING') THEN :usdToVnd
            ELSE ha.rate
        END,
        ha.priceUSD = 
        CASE
            WHEN ha.date >= :today AND ha.homestayAvailabilityStatus NOT IN ('BOOKED', 'PENDING') THEN (ha.priceVND * 1.0) / :usdToVnd
            ELSE ha.priceUSD
        END
    WHERE ha.homestayAvailabilityStatus NOT IN ('BOOKED', 'PENDING')
    AND ha.date >= :yesterday
""")
    void updateRateEveryDay(
            @Param("usdToVnd") BigDecimal usdToVnd,
            @Param("today") LocalDate today,
            @Param("yesterday") LocalDate yesterday
    );


    @Query("""
    SELECT new KMA.BeBookingApp.domain.booking.dto.response.HomestayAvailabilityResponse(
        ha.id,
        ha.homestayId,
        ha.date,
        ha.priceVND,
        ha.priceUSD,
        ha.homestayAvailabilityStatus,
        ha.note,
        ha.rate,
        CASE 
            WHEN ha.date < :currentDate THEN TRUE
            ELSE FALSE
        END
    )
    FROM HomestayAvailability ha
    WHERE ha.homestayId = :homestayId
    ORDER BY ha.date ASC
""")
    List<HomestayAvailabilityResponse> getAvailabilityByHomestayId(
            @Param("homestayId") Long homestayId,
            @Param("currentDate") LocalDate currentDate
    );




    @Query("""
    SELECT ha 
    FROM HomestayAvailability ha 
    WHERE ha.id = :availabilityId 
      AND ha.homestayAvailabilityStatus != :excludedStatus
""")
    Optional<HomestayAvailability> findByIdCus(@Param("availabilityId") Long availabilityId,
                                               @Param("excludedStatus") HomestayAvailabilityStatus excludedStatus);


    @Modifying
    @Query("""
    UPDATE HomestayAvailability ha
    SET ha.rate = :usdToVnd, 
        ha.priceUSD = (ha.priceVND * 1.0) / :usdToVnd
    WHERE ha.id IN :ids
      AND ha.date >= :today 
      AND ha.homestayAvailabilityStatus != :excludedStatus
      AND ha.homestayId = :homestayId
""")
    Integer updateAvailabilityAllPrice(
            @Param("ids") List<Long> ids,
            @Param("usdToVnd") Long priceVnd,
            @Param("excludedStatus") HomestayAvailabilityStatus excludedStatus,
            @Param("today") LocalDate today,
            @Param("homestayId") Long homestayId
    );

    @Modifying
    @Query("""
    UPDATE HomestayAvailability ha
    SET ha.homestayAvailabilityStatus = 'BOOKED'
    WHERE ha.homestayId = :homestayId 
    AND ha.date BETWEEN :checkInDate AND :checkoutDate 
    AND ha.homestayAvailabilityStatus = 'AVAILABLE'
""")
    int markBookedAvailability(Long homestayId, LocalDate checkInDate, LocalDate checkoutDate);


    @Query("""
    SELECT ha FROM  HomestayAvailability ha
    WHERE ha.homestayId = :homestayId 
    AND ha.date BETWEEN :checkInDate AND :checkoutDate 
    AND ha.homestayAvailabilityStatus = 'AVAILABLE'
""")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<HomestayAvailability> findToBook(Long homestayId, LocalDate checkInDate, LocalDate checkoutDate);


    @Modifying
    @Query("""
    UPDATE HomestayAvailability ha
    SET ha.homestayAvailabilityStatus = 'BOOKED'
    WHERE ha.bookingId = :bookingId 
    AND ha.homestayAvailabilityStatus = 'PENDING'
""")
    void markBooked(@Param("bookingId") Long bookingId);

    @Modifying
    @Query("""
    UPDATE HomestayAvailability ha
    SET ha.homestayAvailabilityStatus = 'AVAILABLE', 
        ha.bookingId = NULL
    WHERE ha.bookingId = :bookingId 
      AND ha.homestayAvailabilityStatus = 'PENDING'
""")
    int cancelPendingAvailabilityDay(@Param("bookingId") Long bookingId);



    @Modifying
    @Query("""
    UPDATE HomestayAvailability ha
    SET ha.priceVND = :priceVnd, 
        ha.priceUSD = (:priceVnd * 1.0) / :rate,
        ha.note = :note
    WHERE ha.date >= :today 
      AND ha.homestayId = :homestayId
      AND ha.homestayAvailabilityStatus NOT IN ('BOOKED', 'PENDING')  
      AND ha.id IN :availabilityIds
""")
    int updateAvailability(
            @Param("homestayId") Long homestayId,
            @Param("availabilityIds") List<Long> availabilityIds,
            @Param("rate") BigDecimal rate,
            @Param("priceVnd") Long priceVnd,
            @Param("note") String note,
            @Param("today") LocalDate today
    );


    @Modifying
    @Query("""
    UPDATE HomestayAvailability ha
    SET ha.homestayAvailabilityStatus = 'UNAVAILABLE'
    WHERE ha.date >= :today 
      AND ha.homestayId = :homestayId
      AND ha.homestayAvailabilityStatus NOT IN ('BOOKED', 'PENDING','UNAVAILABLE')  
      AND ha.id IN :availabilityIds
""")
    int disableAvailability(
            @Param("today") LocalDate today,
            @Param("homestayId") Long homestayId,
            @Param("availabilityIds") List<Long> availabilityIds
    );


    @Modifying
    @Query("""
    UPDATE HomestayAvailability ha
    SET ha.homestayAvailabilityStatus = 'AVAILABLE'
    WHERE ha.date >= :today 
      AND ha.homestayId = :homestayId
      AND ha.homestayAvailabilityStatus NOT IN ('BOOKED', 'PENDING','AVAILABLE')  
      AND ha.id IN :availabilityIds
""")
    int enableAvailability(
            @Param("today") LocalDate today,
            @Param("homestayId") Long homestayId,
            @Param("availabilityIds") List<Long> availabilityIds
    );


    @Query("SELECT new KMA.BeBookingApp.domain.homestay.dto.response.PriceRangeResponse(" +
            "MIN(ha.priceVND), MAX(ha.priceVND), MIN(ha.priceUSD), MAX(ha.priceUSD)) " +
            "FROM HomestayAvailability ha " +
            "WHERE ha.date > :now")
    PriceRangeResponse findPriceRangeForAllHomestays(@Param("now") LocalDate now);


    // Lấy ngày lớn nhất
    @Query("SELECT MAX(ha.date) FROM HomestayAvailability ha")
    LocalDate getLatestDate();

    // Lấy giá trị MAX priceVND với điều kiện ngày lớn hơn tham số now
    @Query("SELECT MAX(ha.priceVND) FROM HomestayAvailability ha WHERE ha.date > :now")
    Long getMaxPriceVnd(@Param("now") LocalDate now);


    @Query("""
    SELECT new KMA.BeBookingApp.domain.homestay.dto.response.AvgPriceResponse(
        AVG(ha.priceVND), AVG(ha.priceUSD)
    )
    FROM HomestayAvailability ha 
    WHERE ha.homestayId = :id
""")
    AvgPriceResponse getAvgPrice(@Param("id") Long id);


}
