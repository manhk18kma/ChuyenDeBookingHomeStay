package KMA.BeBookingApp.app.api.booking;

import KMA.BeBookingApp.domain.booking.dto.request.BookingRequest;
import KMA.BeBookingApp.domain.booking.dto.response.BookingResponse;
import KMA.BeBookingApp.domain.booking.service.abtract.BookingService;
import KMA.BeBookingApp.domain.common.dto.ResponseData;
import KMA.BeBookingApp.domain.homestay.dto.request.CreateHomestayRequest;
import KMA.BeBookingApp.domain.homestay.dto.response.ChangeProcessHomestayResponse;
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

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1booking")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Booking Controller", description = "Các API để quản lý Homestay Booking.")
public class BookingController {
    BookingService bookingService;

    @Operation(
            summary = "Tạo mới booking",
            description = "API cho phép người dùng tạo booking mới bằng cách cung cấp thông tin chi tiết."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking được tạo thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "500", description = "Lỗi server, không xử lý được yêu cầu")
    })
    @PostMapping
    public ResponseData<BookingResponse> createBooking(
            @RequestBody @Valid BookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Booking created successfully",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(
            summary = "Cancel a booking",
            description = "Cancel the booking with the specified booking ID. This operation sets the booking status to 'CANCELLED' and updates related records."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Booking cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid booking ID or invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error, failure during the cancellation process")
    })
    @DeleteMapping("/{bookingId}")
    public ResponseData<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return new ResponseData<>(
                HttpStatus.NO_CONTENT.value(),
                "Booking cancelled successfully",
                LocalDateTime.now()
        );
    }



}
