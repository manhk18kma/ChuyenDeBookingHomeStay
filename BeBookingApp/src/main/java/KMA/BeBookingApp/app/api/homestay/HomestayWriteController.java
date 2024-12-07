package KMA.BeBookingApp.app.api.homestay;

import KMA.BeBookingApp.domain.common.dto.ResponseData;
import KMA.BeBookingApp.domain.common.service.UploadService;
import KMA.BeBookingApp.domain.homestay.dto.request.*;
import KMA.BeBookingApp.domain.homestay.dto.response.ChangeProcessHomestayResponse;
import KMA.BeBookingApp.domain.homestay.service.abtract.HomestayWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/homestay-write")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Create homestay controller",
        description = """
                    APIs cho thao tác write database cho homestay, gồm nhiều bước lưu data khi chuyển bước,
                    có thể quay lại bước trước data vẫn giữ nguyên nhưng không thể đi đến bước n + 1.
                    """
)
public class  HomestayWriteController {

    HomestayWriteService homestayService;
    UploadService uploadService;

    private ResponseData<ChangeProcessHomestayResponse> createResponse(ChangeProcessHomestayResponse response, String message, HttpStatus status) {
        return new ResponseData<>(
                status.value(),
                message,
                LocalDateTime.now(),
                response
        );
    }

    @Operation(
            summary = "Bước 1 : Tạo mới 1 homestay",
            description = """
                    Tạo mới 1 homestay để có thể thực hiện các bước tiếp theo
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Homestay created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseData<ChangeProcessHomestayResponse> createHomestay(
            @Parameter(description = "Create homestay request", required = true)
            @RequestBody @Valid CreateHomestayRequest request) {
        ChangeProcessHomestayResponse response = homestayService.createHomestay(request);
        return createResponse(response, "Tạo homestay thành công", HttpStatus.CREATED);
    }




    @Operation(
            summary = "Bước 2: Cập nhật style cho homestay",
            description = """
                    Cập nhật thông tin style cho homestay cụ thể bằng cách sử dụng homestayId,
                    chọn duy nhất 1 và nếu chọn mới thì post id mới lên,
                    id style đã chọn thì isSelect = true tại api get tương ứng
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật style homestay thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Homestay không tồn tại"),
            @ApiResponse(responseCode = "500", description = "Lỗi nội bộ server")
    })
    @PutMapping("{homestayId}/style")
    public ResponseData<ChangeProcessHomestayResponse> updateHomestayStyle(
            @Parameter(description = "ID của homestay cần cập nhật", required = true)
            @PathVariable Long homestayId,
            @RequestBody @Valid UpdateHomestayStyleRequest request) {
        ChangeProcessHomestayResponse response = homestayService.updateHomestayStyle(homestayId, request);
        return createResponse(response, "Cập nhật style homestay thành công", HttpStatus.OK);
    }


    @Operation(
            summary = "Bước 3: Cập nhật spaces cho homestay",
            description = """
                    Cập nhật thông tin không gian cho homestay cụ thể bằng cách sử dụng homestayId,
                    post danh sách id spaces chứa cả nhưng spaces cũ đã chọn, 
                    spaces đã chọn thì isSelected = true trong api get tương ứng
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật space homestay thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Homestay không tồn tại"),
            @ApiResponse(responseCode = "500", description = "Lỗi nội bộ server")
    })
    @PutMapping("{homestayId}/spaces")
    public ResponseData<ChangeProcessHomestayResponse> updateHomestaySpace(
            @Parameter(description = "ID của homestay cần cập nhật", required = true)
            @PathVariable Long homestayId,
            @RequestBody @Valid UpdateHomestaySpaceRequest request) {
        ChangeProcessHomestayResponse response = homestayService.updateHomestaySpace(homestayId, request);
        return createResponse(response, "Cập nhật space homestay thành công", HttpStatus.OK);
    }



    @Operation(
            summary = "Bước 4: Cập nhật tiện nghi cho homestay - amenities",
            description = """
                    Cập nhật thông tin tiện nghi - amenities cho homestay cụ thể bằng cách sử dụng homestayId,
                    post danh sách id amenities chứa cả nhưng amenities cũ đã chọn, 
                    amenities đã chọn thì isSelected = true trong api get tương ứng
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật tiện nghi homestay thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Homestay không tồn tại"),
            @ApiResponse(responseCode = "500", description = "Lỗi nội bộ server")
    })
    @PutMapping("{homestayId}/amenities")
    public ResponseData<ChangeProcessHomestayResponse> updateHomestayAmenity(
            @Parameter(description = "ID của homestay cần cập nhật", required = true)
            @PathVariable Long homestayId,
            @RequestBody @Valid UpdateHomestayAmenityRequest request) {
        ChangeProcessHomestayResponse response = homestayService.updateHomestayAmenity(homestayId, request);
        return createResponse(response, "Cập nhật tiện nghi homestay thành công", HttpStatus.OK);
    }

    @Operation(
            summary = "Bước 5: Cập nhật hình ảnh cho homestay",
            description = """
                    Cập nhật thông tin media hình ảnh video cho homestay cụ thể bằng cách sử dụng homestayId,
                    api get tương ứng trả về media đã post lên trước đó nếu có => xoá cái nào thì thêm id của nó vào UpdateHomestayImageRequest,
                    các media liên quan hoặc media muốn thêm mới thì cứ thêm mới bình thường
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật hình ảnh homestay thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Homestay không tồn tại"),
            @ApiResponse(responseCode = "500", description = "Lỗi nội bộ server")
    })
    @PutMapping("{homestayId}/media")
    public ResponseData<ChangeProcessHomestayResponse> updateHomestayImage(
            @PathVariable Long homestayId,
            @RequestPart(value = "primary" , required = false) MultipartFile primaryFile,
            @RequestPart(value = "related",required = false) List<MultipartFile> relatedFiles,
            @RequestPart("request") @Valid UpdateHomestayImageRequest request
    ) {
        ChangeProcessHomestayResponse response = homestayService.updateHomestayMedia(homestayId, request, primaryFile, relatedFiles);
        return createResponse(response, "Cập nhật hình ảnh homestay thành công", HttpStatus.OK);
    }






    @Operation(
            summary = "Bước 6 : Cập nhật vị trí cho homestay",
            description = """
                    Gửi lên address thông tin search từ api map + address detail tùy chỉnh cho người dùng trong trường hợp api map không có.
                    Bắt buộc phải có kinh độ vĩ độ
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật vị trí homestay thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Homestay không tồn tại"),
            @ApiResponse(responseCode = "500", description = "Lỗi nội bộ server")
    })
    @PutMapping("{homestayId}/locations")
    public ResponseData<ChangeProcessHomestayResponse> updateHomestayLocation(
            @Parameter(description = "ID của homestay cần cập nhật", required = true)
            @PathVariable Long homestayId,
            @RequestBody @Valid UpdateHomestayLocationRequest request) {
        ChangeProcessHomestayResponse response = homestayService.updateHomestayLocation(homestayId, request);
        return createResponse(response, "Cập nhật vị trí homestay thành công", HttpStatus.OK);
    }



    @Operation(
            summary = "Bước 7 : Cập nhật thông tin chung cho homestay",
            description = "Cập nhật các thông tin như tên, mô tả, và số lượng khách tối đa của homestay sử dụng homestayId."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thông tin homestay thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Homestay không tồn tại"),
            @ApiResponse(responseCode = "500", description = "Lỗi nội bộ server")
    })
    @PutMapping("{homestayId}/information")
    public ResponseData<ChangeProcessHomestayResponse> updateHomestayInformation(
            @Parameter(description = "ID của homestay cần cập nhật", required = true)
            @PathVariable Long homestayId,
            @RequestBody @Valid UpdateHomestayInformationRequest request) {
        // Cập nhật các thông tin chi tiết về homestay
        ChangeProcessHomestayResponse response = homestayService.updateHomestayInformation(homestayId, request);
        return createResponse(response, "Cập nhật thông tin homestay thành công", HttpStatus.OK);
    }

    @Operation(
            summary = "Bước 8: Xác nhận quy trình homestay",
            description = "Xác nhận quy trình homestay sau khi đã cập nhật thông tin chung. Homestay sẽ được chuyển sang trạng thái sẵn sàng."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xác nhận quy trình homestay thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Homestay không tồn tại"),
            @ApiResponse(responseCode = "500", description = "Lỗi nội bộ server")
    })
    @PutMapping("{homestayId}/confirmation")
    public ResponseData<ChangeProcessHomestayResponse> confirmHomestayProcess(
            @Parameter(description = "ID của homestay cần xác nhận quy trình", required = true)
            @PathVariable Long homestayId,
            @RequestBody @Valid ConfirmHomestayProcessRequest request) {

        // Gọi service xử lý logic
        ChangeProcessHomestayResponse response = homestayService.confirmHomestayProcess(homestayId, request);
        return createResponse(response, "Xác nhận quy trình homestay thành công", HttpStatus.OK);
    }

    @Operation(
            summary = "Xóa Homestay",
            description = "API này xóa một homestay dựa trên ID được cung cấp.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Xóa homestay thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy homestay"),
                    @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
            }
    )
    @DeleteMapping("/{homestayId}")
    public ResponseData<?> deleteHomestay(
            @Parameter(description = "ID của homestay cần xóa", required = true)
            @PathVariable("homestayId") Long homestayId) {
        homestayService.deleteHomestay(homestayId);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Xóa homestay thành công",
                LocalDateTime.now()
        );
    }

}
