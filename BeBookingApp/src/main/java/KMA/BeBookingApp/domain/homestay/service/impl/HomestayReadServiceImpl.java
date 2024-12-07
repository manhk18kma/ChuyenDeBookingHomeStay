package KMA.BeBookingApp.domain.homestay.service.impl;

import KMA.BeBookingApp.domain.booking.entity.HomestayAvailability;
import KMA.BeBookingApp.domain.booking.repository.HomestayAvailabilityRepository;
import KMA.BeBookingApp.domain.booking.service.FetchRating.ExchangeRate;
import KMA.BeBookingApp.domain.booking.service.FetchRating.ExchangeRateService;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStatus;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStepStatus;
import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import KMA.BeBookingApp.domain.common.exception.AppException;
import KMA.BeBookingApp.domain.homestay.dto.request.SearchHomestayRequest;
import KMA.BeBookingApp.domain.homestay.dto.response.*;
import KMA.BeBookingApp.domain.homestay.entity.Homestay;
import KMA.BeBookingApp.domain.homestay.repository.*;
import KMA.BeBookingApp.domain.homestay.service.abtract.HomestayReadService;
import KMA.BeBookingApp.domain.user.entity.User;
import KMA.BeBookingApp.domain.user.service.abtract.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.Style;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class HomestayReadServiceImpl implements HomestayReadService {
    HomestayRepository homestayRepository;
    AuthService authService;
    HomestayStyleRepository homestayStyleRepository;
    SpaceRepository spaceRepository;
    AmenityRepository amenityRepository;
    HomestayAvailabilityRepository homestayAvailabilityRepository;
    ExchangeRateService exchangeRateService;
    MediaRepository mediaRepository;
    @Override
    public HomestayDetailOwnerResponse getHomestayDetailOwner(Long homestayId) {
        Homestay homestay = validateOwnerHomestayAndGet(homestayId);
        validateStepHomestay(homestay);
        HomestayDetailOwnerResponse response = homestayRepository.getHomestayDetailOwner(homestayId).get();
//                .orElseThrow(()->new AppException(AppErrorCode.RESOURCE_NOT_FOUND));
        HomestayStyleResponse homestayStyleResponse = homestayStyleRepository.getHomestayStyleResponseByHomestayId(homestayId);


        List<SpaceResponse> spaceResponses = spaceRepository.getAllSpaceOfHomestay(homestayId);
        List<AmenityResponse> amenityResponses = amenityRepository.getAllAmenityOfHomestay(homestayId);

        response.setHomestayStyle(homestayStyleResponse);
        response.setAmenities(amenityResponses);
        response.setSpaces(spaceResponses);
        return response;
    }

    @Override
    public SearchFilterResponse getAvailableFilters() {
        List<HomestayStyleResponse> styleResponses = homestayStyleRepository.getAllToFilters();
        List<AmenityResponse> amenityResponses = amenityRepository.getAllToFilters();
        List<SpaceResponse> spaceResponses = spaceRepository.getAllToFilters();
        PriceRangeResponse priceRangeResponse = homestayAvailabilityRepository.findPriceRangeForAllHomestays(LocalDate.now());
        priceRangeResponse.setRate(exchangeRateService.getExchangeRates().getUsdToVnd());

        return SearchFilterResponse.builder()
                .amenities(amenityResponses)
                .priceRange(priceRangeResponse)
                .spaces(spaceResponses)
                .styles(styleResponses)
                .build();
    }



    @Override
    public List<HomestayResponse> searchHomestays(SearchHomestayRequest request) {
        // Đảm bảo latitude và longitude không phải null
        Double latitude = request.getLatitude() != null ? request.getLatitude() : null;
        Double longitude = request.getLongitude() != null ? request.getLongitude() : null;

        System.out.println("latitude" + latitude);
        System.out.println("longitude" + longitude);

// Đặt mặc định cho ngày checkin và checkout
        // Tính toán số đêm giữa checkinDate và checkoutDate
        Integer nights = (request.getCheckinDate() != null && request.getCheckoutDate() != null) ?
                (int) ChronoUnit.DAYS.between(request.getCheckinDate(), request.getCheckoutDate()) + 1 : 1;

        LocalDate checkinDate = request.getCheckinDate() != null ? request.getCheckinDate() : LocalDate.now();
        LocalDate checkoutDate = request.getCheckoutDate() != null ? request.getCheckoutDate() : homestayAvailabilityRepository.getLatestDate();

        System.out.println("checkinDate" + checkinDate);
        System.out.println("checkoutDate" + checkoutDate);

// Đặt mặc định cho số lượng khách
        Integer guests = request.getGuests() != null ? request.getGuests() : 0;

// Đặt mặc định cho giá trị minPriceVnd
        Long minPriceVnd = request.getMinPriceVnd() != null ? request.getMinPriceVnd() : 0;

// Đặt mặc định cho giá trị maxPriceVnd, nếu null thì lấy giá trị từ repo
        Long maxPriceVnd = request.getMaxPriceVnd() != null ? request.getMaxPriceVnd() : homestayAvailabilityRepository.getMaxPriceVnd(LocalDate.now());



        System.out.println("guests" + guests);
        System.out.println("minPriceVnd" + minPriceVnd);
        System.out.println("maxPriceVnd" + maxPriceVnd);
        System.out.println("nights" + nights);

// Bán kính mặc định
        Integer radius = 15000;

        Long styleIdRequest = request.getStyleId() != null ? request.getStyleId() : null;
        List<Long> styleIds = new ArrayList<>();
        if (styleIdRequest != null) {
            styleIds.add(styleIdRequest);
        } else {
            styleIds = homestayStyleRepository.getAllIds();
        }

        List<Long> spaceIds = request.getSpaceIds() != null && !request.getSpaceIds().isEmpty() ? request.getSpaceIds() : spaceRepository.getAllIds();

        List<Long> amenityIds = request.getAmenityIds() != null && !request.getAmenityIds().isEmpty() ? request.getAmenityIds() : amenityRepository.getAllIds();

        System.out.println("styleIds" + styleIds);
        System.out.println("spaceIds" + spaceIds);
        System.out.println("amenityIds" + amenityIds);




        System.out.println(nights);
        List<Long> results = new ArrayList<>();
        if (latitude != null && longitude != null) {
             results = homestayRepository.searchWithLocation(
                    latitude,
                    longitude,
                    minPriceVnd,
                    maxPriceVnd,
                    styleIds,
                    spaceIds,
                    amenityIds,
                    guests,
                    checkinDate,
                    checkoutDate,
                    radius,
                    nights
            );


            System.out.println("Location " +results);
        } else {
             results = homestayRepository.searchWithoutLocation(
                    minPriceVnd,
                    maxPriceVnd,
                    styleIds,
                    spaceIds,
                    amenityIds,
                    guests,
                    checkinDate,
                    checkoutDate,
                    nights
            );

            System.out.println("Without Location " +results);
        }
        return buildSearchHomestayResponse(results);
    }



    private Homestay validateOwnerHomestayAndGet(Long homestayId){
        User user = authService.validateAndGetUserByAuthenticationName();
        Homestay homestay = homestayRepository.findByIdCus(homestayId)
                .orElseThrow(() -> new AppException(AppErrorCode.RESOURCE_NOT_FOUND));

        if (!homestay.getHostId().equals(user.getId())) {
            throw new AppException(AppErrorCode.UNAUTHORIZED);
        }
        return homestay;
    }

    private void validateStepHomestay(Homestay homestay) {
        if (homestay.getHomestayStatus().equals(HomestayStatus.CREATING)) {
            throw new AppException(AppErrorCode.INVALID_HOMESTAY_STEP);
        }
    }


    private List<HomestayResponse> buildSearchHomestayResponse(List<Long> homestayIds){

        List<HomestayResponse> responses = homestayRepository.buildSearchHomestayResponse(homestayIds);
        responses.stream().forEach(response -> {
            response.setMedias(mediaRepository.getAllMediaOfHomestayCus(response.getId()));
            AvgPriceResponse avgPriceResponse = homestayAvailabilityRepository.getAvgPrice(response.getId() );
            response.setAvgUsdPerNight(avgPriceResponse.getAvgUsdPerNight());
            response.setAvgVndPerNight(avgPriceResponse.getAvgVndPerNight());

            if(response.getMedias().isEmpty()){
                response.getMedias().add(0 , HomestayMediaResponse.builder()
                                .url("https://tse4.mm.bing.net/th?id=OIP.nEUTJHCsxG7SGctqFqS_OQHaHa&pid=Api&P=0&h=220")
                        .build());
            }

            Random random = new Random();

            double avgStars = 3 + (5 - 3) * random.nextDouble();

            BigDecimal roundedAvgStars = BigDecimal.valueOf(avgStars).setScale(1, RoundingMode.HALF_UP);

            response.setAvgStars(roundedAvgStars);
        });
        return responses;
    }
}
