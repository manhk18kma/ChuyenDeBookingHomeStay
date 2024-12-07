package KMA.BeBookingApp.domain.booking.service.impl;

import KMA.BeBookingApp.domain.booking.dto.request.*;
import KMA.BeBookingApp.domain.booking.dto.response.HoldAvailabilityResponse;
import KMA.BeBookingApp.domain.booking.dto.response.HomestayAvailabilityResponse;
import KMA.BeBookingApp.domain.booking.dto.response.UpdateAvailabilityResponse;
import KMA.BeBookingApp.domain.booking.entity.HomestayAvailability;
import KMA.BeBookingApp.domain.booking.repository.HomestayAvailabilityRepository;
import KMA.BeBookingApp.domain.booking.service.FetchRating.ExchangeRate;
import KMA.BeBookingApp.domain.booking.service.FetchRating.ExchangeRateService;
import KMA.BeBookingApp.domain.booking.service.abtract.HomestayAvailabilityService;
import KMA.BeBookingApp.domain.common.enumType.booking.BookingStatus;
import KMA.BeBookingApp.domain.common.enumType.booking.HomestayAvailabilityStatus;
import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import KMA.BeBookingApp.domain.common.exception.AppException;
import KMA.BeBookingApp.domain.homestay.entity.Homestay;
import KMA.BeBookingApp.domain.homestay.repository.HomestayRepository;
import KMA.BeBookingApp.domain.user.entity.User;
import KMA.BeBookingApp.domain.user.service.abtract.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomestayAvailabilityServiceImpl implements HomestayAvailabilityService {
    HomestayAvailabilityRepository homestayAvailabilityRepository;
    ExchangeRateService exchangeRateService;
    HomestayRepository homestayRepository;
    AuthService authService;

    @Override
    public void generateAvailabilityForNextYear(Long homestayId , Long defaultPrice){
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRates();
        List<HomestayAvailability> availabilityList = new ArrayList<>();

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            HomestayAvailability availability = HomestayAvailability.builder()
                    .homestayId(homestayId)
                    .date(currentDate)
                    .rate(exchangeRate.getUsdToVnd())
                    .priceUSD(BigDecimal.valueOf(defaultPrice).divide(exchangeRate.getUsdToVnd(), 2, RoundingMode.HALF_UP))
                    .homestayAvailabilityStatus(HomestayAvailabilityStatus.AVAILABLE)
                    .priceVND(defaultPrice)
                    .build();

            availabilityList.add(availability);
            currentDate = currentDate.plusDays(1);
        }
        homestayAvailabilityRepository.saveAll(availabilityList);
    }



    @Override
    public  Map<YearMonth, List<HomestayAvailabilityResponse>> getAvailabilityByHomestayId(Long homestayId) {
        List<HomestayAvailabilityResponse> homestayAvailabilityResponseList
                 = homestayAvailabilityRepository.getAvailabilityByHomestayId(homestayId , LocalDate.now());
        Map<YearMonth, List<HomestayAvailabilityResponse>> groupedByMonth = homestayAvailabilityResponseList.stream()
                .collect(Collectors.groupingBy(
                        response -> YearMonth.from(response.getDate()),
                        TreeMap::new,
                        Collectors.toList()
                ));

        return groupedByMonth;
    }

    @Override
    @Transactional
    public UpdateAvailabilityResponse updateAvailability(Long homestayId, UpdateAvailabilityRequest request) {
        validateOwnerAndGetHomestay(homestayId);
        BigDecimal rate = exchangeRateService.getExchangeRates().getUsdToVnd();

        // Cập nhật thông tin availability
        int affectedRows = homestayAvailabilityRepository.updateAvailability(
                homestayId,
                request.getAvailabilityIds(),
                rate,
                request.getPriceVND(),
                request.getNote(),
                LocalDate.now()
        );

        if (affectedRows != request.getAvailabilityIds().size()) {
            throw new AppException(AppErrorCode.UNAUTHORIZED);
        }

        return UpdateAvailabilityResponse.builder()
                .effectedIds(request.getAvailabilityIds())
                .homestayId(homestayId)
                .build();
    }



    @SneakyThrows
    @Override
    public HoldAvailabilityResponse checkAvailabilityForBooking(CheckAvailabilityRequest request) {

        LocalDate checkInDate = request.getCheckInDate();
        LocalDate checkoutDate = request.getCheckoutDate();
        Long homestayId = request.getHomestayId();
        if (!validateCheckinCheckoutDate(checkInDate, checkoutDate)) {
            throw new AppException(AppErrorCode.BOOKING_INVALID);
        }

        int days = (int) ChronoUnit.DAYS.between(checkInDate, checkoutDate) + 1;
        List<HomestayAvailability> homestayAvailabilities = homestayAvailabilityRepository.findToBook(homestayId, checkInDate, checkoutDate);

        if (homestayAvailabilities.size() != days) {
            throw new AppException(AppErrorCode.BOOKING_INVALID);
        }

        Long amountVnd = homestayAvailabilities.stream()
                .mapToLong(HomestayAvailability::getPriceVND)
                .sum();

        BigDecimal amountUsd = homestayAvailabilities.stream()
                .map(HomestayAvailability::getPriceUSD)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


//        Thread.sleep(10000);

        for (HomestayAvailability availability : homestayAvailabilities) {
            availability.setHomestayAvailabilityStatus(HomestayAvailabilityStatus.PENDING);
            availability.setBookingId(request.getBookingId());
        }

        homestayAvailabilityRepository.saveAll(homestayAvailabilities);

        return HoldAvailabilityResponse.builder()
                .nights(days)
                .rateToUsd(homestayAvailabilities.get(0).getRate())
                .totalPriceVnd(amountVnd)
                .totalPriceUsd(amountUsd)
                .priceVndPerNight(amountVnd / days)
                .priceUsdPerNight(amountUsd.divide(BigDecimal.valueOf(days), RoundingMode.HALF_UP))
                .build();
    }

    @Override
    public void markBooked(Long bookingId) {
        homestayAvailabilityRepository.markBooked(bookingId);
    }

    @Override
    @Transactional
    public void cancelPendingAvailabilityDay(Long bookingId) {
        int affectedRows = homestayAvailabilityRepository.cancelPendingAvailabilityDay(bookingId);
    }

    @Override
    @Transactional
    public UpdateAvailabilityResponse disableAvailability(Long homestayId, DisableAvailabilityRequest request) {
        validateOwnerAndGetHomestay(homestayId);
        int affectedRows = homestayAvailabilityRepository.disableAvailability(LocalDate.now() ,homestayId , request.getAvailabilityIds());
        if (affectedRows != request.getAvailabilityIds().size()) {
            throw new AppException(AppErrorCode.UNAUTHORIZED);
        }

        return UpdateAvailabilityResponse.builder()
                .effectedIds(request.getAvailabilityIds())
                .homestayId(homestayId)
                .build();
    }

    @Override
    @Transactional
    public UpdateAvailabilityResponse enableAvailability(Long homestayId, EnableAvailabilityRequest request) {
        validateOwnerAndGetHomestay(homestayId);
        int affectedRows = homestayAvailabilityRepository.enableAvailability(LocalDate.now() ,homestayId , request.getAvailabilityIds());
        if (affectedRows != request.getAvailabilityIds().size()) {
            throw new AppException(AppErrorCode.UNAUTHORIZED);
        }

        return UpdateAvailabilityResponse.builder()
                .effectedIds(request.getAvailabilityIds())
                .homestayId(homestayId)
                .build();
    }


    private boolean validateCheckinCheckoutDate(LocalDate checkInDate, LocalDate checkoutDate) {
        LocalDate now = LocalDate.now();
        if (checkInDate.isBefore(now)) {
            return false;
        }
        if (checkoutDate.isBefore(checkInDate)) {
            return false;
        }
        if (checkoutDate.equals(checkInDate)) {
            return false;
        }

        return true;
    }



    //        int markBookedAvailability = homestayAvailabilityRepository.markBookedAvailability(homestayId, checkInDate, checkoutDate);
//        if (markBookedAvailability != days) {
//            throw new AppException(AppErrorCode.BOOKING_INVALID);
//        }


    private HomestayAvailability validateOwnerAndGetAvailability(Long homestayId, Long availabilityId) {
        User user = authService.validateAndGetUserByAuthenticationName();
        Homestay homestay = homestayRepository.findByIdCus(homestayId)
                .orElseThrow(() -> new AppException(AppErrorCode.RESOURCE_NOT_FOUND));
        HomestayAvailability homestayAvailability = homestayAvailabilityRepository.findByIdCus(availabilityId, HomestayAvailabilityStatus.BOOKED)
                .orElseThrow(() -> new AppException(AppErrorCode.RESOURCE_NOT_FOUND));

        boolean isOwner = homestay.getHostId().equals(user.getId());
        boolean isMatchingHomestay = homestayAvailability.getHomestayId().equals(homestayId);

        if (!isOwner || !isMatchingHomestay) {
            throw new AppException(AppErrorCode.UNAUTHORIZED);
        }

        return homestayAvailability;
    }

    private Homestay validateOwnerAndGetHomestay(Long homestayId) {
        User user = authService.validateAndGetUserByAuthenticationName();
        Homestay homestay = homestayRepository.findByIdCus(homestayId)
                .orElseThrow(() -> new AppException(AppErrorCode.RESOURCE_NOT_FOUND));

        boolean isOwner = homestay.getHostId().equals(user.getId());

        if (!isOwner ) {
            throw new AppException(AppErrorCode.UNAUTHORIZED);
        }

        return homestay;
    }




    //helper function
    @Override
//    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(cron = "0 54 1 * * ?")
    @Transactional
    public void runJobAtMidnightToUpdateRate() {
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRates();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        homestayAvailabilityRepository.updateRateEveryDay(exchangeRate.getUsdToVnd(), today, yesterday);
    }




}
