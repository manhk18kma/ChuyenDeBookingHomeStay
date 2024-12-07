package KMA.BeBookingApp.app.api.booking;

import KMA.BeBookingApp.domain.booking.dto.request.DisableAvailabilityRequest;
import KMA.BeBookingApp.domain.booking.dto.request.EnableAvailabilityRequest;
import KMA.BeBookingApp.domain.booking.dto.request.UpdateAllPriceRequest;
import KMA.BeBookingApp.domain.booking.dto.request.UpdateAvailabilityRequest;
import KMA.BeBookingApp.domain.booking.dto.response.HomestayAvailabilityResponse;
import KMA.BeBookingApp.domain.booking.service.FetchRating.ExchangeRate;
import KMA.BeBookingApp.domain.booking.service.FetchRating.ExchangeRateService;
import KMA.BeBookingApp.domain.booking.service.abtract.HomestayAvailabilityService;
import KMA.BeBookingApp.domain.common.dto.ResponseData;
import KMA.BeBookingApp.domain.homestay.dto.response.HomestayCardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Homestay Availability Controller", description = "Các API để quản lý Homestay Availability.")
public class HomestayAvailabilityController {
    HomestayAvailabilityService homestayAvailabilityService;



    @Operation(summary = "Lấy danh sách availability theo tháng", description = "Trả về danh sách trạng thái Homestay Availability theo tháng")
    @GetMapping("/homestays/{homestayId}/availability")
    public ResponseData<Map<YearMonth, List<HomestayAvailabilityResponse>>> getHomestayAvailability(
            @PathVariable Long homestayId) {
        Map<YearMonth, List<HomestayAvailabilityResponse>> responses = homestayAvailabilityService.getAvailabilityByHomestayId(homestayId);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Lấy danh sách availability thành công",
                LocalDateTime.now(),
                responses
        );
    }

    @Operation(summary = "Cập nhật thông tin HomestayAvailability theo ID và trả về danh sách theo tháng")
    @PutMapping("/homestays/{homestayId}/availability")
    public ResponseData<?> updateAvailability(
            @PathVariable Long homestayId,
            @RequestBody @Valid UpdateAvailabilityRequest request) {
        homestayAvailabilityService.updateAvailability(homestayId, request);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Cập nhật thông tin HomestayAvailability thành công",
                LocalDateTime.now()
        );
    }



    @Operation(
            summary = "Kích hoạt khả năng có sẵn ngày cho thuê của homestay",
            description = "API này sẽ kích hoạt khả năng ngày cho thuê  có sẵn của homestay theo ID."
    )
    @DeleteMapping("/homestays/{homestayId}/enable-availability")
    public ResponseData<?> enableAvailability(
            @PathVariable Long homestayId,
            @RequestBody EnableAvailabilityRequest request
    ) {
        homestayAvailabilityService.enableAvailability(homestayId, request);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Kích hoạt khả năng có sẵn thành công.",
                LocalDateTime.now()
        );
    }

    @Operation(
            summary = "Tắt khả năng có sẵn ngày cho thuê  của homestay",
            description = "API này sẽ tắt khả năng có sẵn ngày cho thuê  của homestay theo ID."
    )
    @DeleteMapping("/homestays/{homestayId}/disable-availability")
    public ResponseData<?> disableAvailability(
            @PathVariable Long homestayId,
            @RequestBody DisableAvailabilityRequest request
    ) {
        homestayAvailabilityService.disableAvailability(homestayId, request);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Khả năng có sẵn đã bị tắt.",
                LocalDateTime.now()
        );
    }


}
