package KMA.BeBookingApp.app.api.homestay;

import KMA.BeBookingApp.domain.common.dto.ResponseData;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayCardType;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStatus;
import KMA.BeBookingApp.domain.common.validation.EnumValue;
import KMA.BeBookingApp.domain.homestay.dto.response.*;
import KMA.BeBookingApp.domain.homestay.service.abtract.HomestayDataService;
import KMA.BeBookingApp.domain.homestay.service.abtract.HomestayReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/homestay-read")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Homestay Create Data Controller", description = "Quản lý các dữ liệu liên quan đến các bước tạo Homestay.")
public class HomestayReadController {

    HomestayDataService homestayDataCreateService;
    HomestayReadService homestayReadService;

    @Operation(
            summary = "Bước 1: Lấy danh sách homestay của user",
            description = "Có thể là các homestay đang trong quá trình nhập thông tin và có thể là những homestay đã hoàn thành thông tin"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Homestay created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping()
    public ResponseData<Map<String , List<HomestayCardResponse>>> getAllHomestayOfAUser(
            @RequestParam(defaultValue = "DEFAULT") @EnumValue(name = "status", enumClass = HomestayStatus.class) String status
    ) {

        Map<String , List<HomestayCardResponse>>  response = homestayDataCreateService.getAllHomestayOfAUser(status);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Homestay created successfully",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Bước 2: Lấy HomestayStyle của Homestay",
            description = """
                    Api trả về danh sách homestayStyle và nếu đã chọn trước đó thì is Selected = true
                    """,
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lấy phong cách thành công"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy homestay với ID đã cho"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
            })
    @GetMapping("/{homestayId}/homestay-style")
    public ResponseData<?> getAllHomestayStyle(
            @PathVariable Long homestayId
    ) {
        // Lấy các phong cách homestay
        Map<String, List<HomestayStyleResponse>> response = homestayDataCreateService.getAllHomestayStyle(homestayId);

        // Trả về kết quả
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Lấy phong cách homestay thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Bước 3 : Lấy thông tin không gian của Homestay",
            description = "API này cung cấp các thông tin không gian của homestay theo ID. Nếu id nào đã chọn trước đó rồi thì isSelected = true",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Danh sách không gian được trả về thành công"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy homestay với ID đã cho"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
            })
    @GetMapping("/{homestayId}/spaces")
    public ResponseData<?> getAllSpace(
            @PathVariable Long homestayId
    ) {
        Map<String, List<SpaceResponse>> response = homestayDataCreateService.getAllSpace(homestayId);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Lấy không gian homestay thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Bước 4 : Lấy các tiện ích của Homestay",
            description = "Truy vấn API để lấy các tiện ích của homestay theo ID. Nếu id nào đã chọn trước đó rồi thì isSelected = true",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Danh sách tiện ích được trả về thành công"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy homestay với ID đã cho"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
            })
    @GetMapping("/{homestayId}/amenities")
    public ResponseData<?> getAllAmenity(
            @PathVariable Long homestayId
    ) {
        Map<String, List<AmenityResponse>> response = homestayDataCreateService.getAllAmenity(homestayId);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Lấy tiện ích homestay thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Bước 5 : Lấy các media của Homestay",
            description = "API này trả về thông tin về các media (ảnh/video) của homestay theo ID. Data trả về có id và phục vụ cho việc có muốn xoá hay không",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Danh sách media homestay thành công"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy homestay với ID đã cho"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
            })
    @GetMapping("/{homestayId}/media")
    public ResponseData<?> getAllMedia(
            @PathVariable Long homestayId
    ) {
        Map<String, List<HomestayMediaResponse>> response = homestayDataCreateService.getAllMedia(homestayId);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Lấy media homestay thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Bước 6: Lấy vị trí của Homestay",
            description = "API này cung cấp thông tin về vị trí của homestay theo ID.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vị trí homestay thành công"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy homestay với ID đã cho"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
            })
    @GetMapping("/{homestayId}/location")
    public ResponseData<?> getHomestayLocation(
            @PathVariable Long homestayId
    ) {
        // Lấy vị trí homestay
        HomestayLocationResponse response = homestayDataCreateService.getHomestayLocation(homestayId);

        // Trả về kết quả
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Lấy vị trí homestay thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Bước 7: Lấy thông tin chung của Homestay",
            description = "API này trả về các thông tin cơ bản của homestay như tên, mô tả, giá cả.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Thông tin homestay thành công"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy homestay với ID đã cho"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
            })
    @GetMapping("/{homestayId}/information")
    public ResponseData<?> getHomestayInformation(
            @PathVariable Long homestayId
    ) {
        // Lấy thông tin chi tiết của homestay
        HomestayInformationResponse response = homestayDataCreateService.getHomestayInformation(homestayId);

        // Trả về kết quả
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Lấy thông tin homestay thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Bươc 8: Lấy thông tin xác nhận của Homestay",
            description = "API này trả về thông tin xác nhận của homestay cho người dùng.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Xác nhận homestay thành công"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy homestay với ID đã cho"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
            })
    @GetMapping("/{homestayId}/confirmation")
    public ResponseData<?> getHomestayConfirmation(
            @PathVariable Long homestayId
    ) {
        // Lấy thông tin xác nhận
        HomestayConfirmationResponse response = homestayDataCreateService.getHomestayConfirmation(homestayId);

        // Trả về kết quả
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Lấy thông tin xác nhận homestay thành công",
                LocalDateTime.now(),
                response
        );
    }


    @Operation(
            summary = "Lấy thông tin chi tiết Homestay dành cho chủ homestay",
            description = "API này cho phép người dùng lấy thông tin chi tiết của một homestay thông qua ID của homestay.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lấy thông tin homestay thành công."),
                    @ApiResponse(responseCode = "404", description = "Homestay không tồn tại."),
                    @ApiResponse(responseCode = "500", description = "Lỗi hệ thống.")
            }
    )
    @GetMapping("/{homestayId}/detail-owner")
    public ResponseData<?> getHomestayDetail(
            @Parameter(description = "ID của Homestay cần lấy thông tin", required = true)
            @PathVariable Long homestayId
    ) {
        HomestayDetailOwnerResponse response = homestayReadService.getHomestayDetailOwner(homestayId);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Lấy thông tin homestay thành công",
                LocalDateTime.now(),
                response
        );
    }
}
