package KMA.BeBookingApp.app.api.homestay;

import KMA.BeBookingApp.domain.common.dto.ResponseData;
import KMA.BeBookingApp.domain.homestay.dto.request.SearchHomestayRequest;
import KMA.BeBookingApp.domain.homestay.dto.response.HomestayResponse;
import KMA.BeBookingApp.domain.homestay.dto.response.SearchFilterResponse;
import KMA.BeBookingApp.domain.homestay.service.abtract.HomestayReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/homestay-search")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Search homestay controller",
        description = """
                    APIs cho việc lấy ra bộ lọc và search homestay
                    """
)
public class HomestaySearchController {

    HomestayReadService homestayReadService;

    @Operation(
            summary = "Lấy danh sách các bộ lọc tìm kiếm",
            description = "Trả về các bộ lọc có sẵn để người dùng có thể chọn (style, tiện ích, mức giá, vị trí, ...)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Danh sách bộ lọc được lấy thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống nội bộ")
    })
    @GetMapping("/filters")
    public ResponseData<SearchFilterResponse> getAvailableFilters() {
        SearchFilterResponse response = homestayReadService.getAvailableFilters();

        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Lấy danh sách bộ lọc thành công",
                LocalDateTime.now(),
                response
        );
    }






    @Operation(summary = "Search homestays based on various filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Homestays found"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping("/search")
    public ResponseData<List<HomestayResponse>> searchHomestays(
            @RequestParam(required = false)
            @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
            @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
            Double latitude,

            @RequestParam(required = false)
            @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
            @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
            Double longitude,

            @RequestParam(required = false)
            @FutureOrPresent(message = "Check-in date must be today or in the future")
            LocalDate checkinDate,

            @RequestParam(required = false)
            @FutureOrPresent(message = "Check-out date must be today or in the future")
            LocalDate checkoutDate,

            @RequestParam(required = false)
            @Min(value = 1, message = "Guests must be at least 1")
            Integer guests,

            @RequestParam(required = false)
            @Min(value = 0, message = "Minimum price must be non-negative")
            Long minPriceVnd,

            @RequestParam(required = false)
            @Min(value = 0, message = "Maximum price must be non-negative")
            Long maxPriceVnd,

            @RequestParam(required = false)
            @Positive(message = "Style ID must be a positive number")
            Long styleId,

            @RequestParam(required = false)
            List<Long> spaceIds,

            @RequestParam(required = false)
            List<Long> amenityIds
    ) {
        // Tạo đối tượng filter từ các tham số nhận được
        SearchHomestayRequest request = SearchHomestayRequest.builder()
                .latitude(latitude)
                .longitude(longitude)
                .checkinDate(checkinDate)
                .checkoutDate(checkoutDate)
                .guests(guests)
                .minPriceVnd(minPriceVnd)
                .maxPriceVnd(maxPriceVnd)
                .styleId(styleId)
                .spaceIds(spaceIds)
                .amenityIds(amenityIds)
                .build();
        System.out.println(request);

        // Gọi service để tìm kiếm homestay theo bộ lọc
        List<HomestayResponse> homestays = homestayReadService.searchHomestays(request);

        // Trả về kết quả tìm kiếm
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Homestays found",
                LocalDateTime.now(),
                homestays
        );
    }



}
