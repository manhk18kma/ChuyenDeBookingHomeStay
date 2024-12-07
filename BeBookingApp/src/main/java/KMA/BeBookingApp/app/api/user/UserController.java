package KMA.BeBookingApp.app.api.user;

import KMA.BeBookingApp.domain.common.dto.ResponseData;
import KMA.BeBookingApp.domain.user.dto.request.ChangePrivateInfoRequest;
import KMA.BeBookingApp.domain.user.dto.request.InitUsernameRequest;
import KMA.BeBookingApp.domain.user.dto.response.FailedAttemptsResponse;
import KMA.BeBookingApp.domain.user.service.abtract.UserService;
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

@RestController
@RequestMapping("/api/v1/users")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Các API để quản lý người dùng.")
public class UserController {

    UserService userService;

//    @Operation(
//            summary = "Thay đổi tên người dùng - Cần access token",
//            description = "API này cho phép người dùng thay đổi tên đăng nhập của mình. Cung cấp username mới thông qua yêu cầu. Chú ý mỗi user chỉ được thay đổi user name 1 lần",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Thay đổi tên người dùng thành công"),
//                    @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
//                    @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
//            }
//    )
//    @PostMapping("/change-username")
//    public ResponseData<?> initUsername(@Parameter(description = "Yêu cầu khởi tạo username", required = true)
//                                               @RequestBody @Valid InitUsernameRequest request) {
//        userService.initUsername(request);
//        return new ResponseData<>(
//                HttpStatus.OK.value(),
//                "Thay đổi username thành công",
//                LocalDateTime.now()
//        );
//    }



    @Operation(
            summary = "Lấy số lần đăng nhập thất bại của người dùng - Cần access token",
            description = "API này cho phép lấy số lần đăng nhập thất bại của người dùng bằng cách sử dụng username hoặc email.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lấy thông tin số lần đăng nhập thất bại thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng"),
                    @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
            }
    )
    @GetMapping("/failed-attempts")
    public ResponseData<?> getFailedAttemptsOfUser(
            @Parameter(description = "Tên đăng nhập hoặc email của người dùng", required = true)
            @RequestParam("usernameOrEmail") String usernameOrEmail) {

        FailedAttemptsResponse response = userService.getFailedAttempts(usernameOrEmail);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Lấy số lần đăng nhập thất bại thành công",
                LocalDateTime.now(),
                response
        );
    }


    @Operation(
            summary = "Lấy thông tin cá nhân của người dùng - Cần access token",
            description = "API này trả về thông tin cá nhân của người dùng đã xác thực.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công"),
                    @ApiResponse(responseCode = "401", description = "Người dùng chưa được xác thực"),
                    @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
            }
    )
    @GetMapping("/private-info")
    public ResponseData<?> getPrivateInfo() {
        var response = userService.getPrivateInfo();
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Lấy thông tin cá nhân thành công",
                LocalDateTime.now(),
                response
        );
    }


    @Operation(
            summary = "Cập nhật thông tin cá nhân của người dùng - Cần access token ",
            description = "API này cho phép người dùng đã xác thực thay đổi thông tin cá nhân của họ.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cập nhật thông tin thành công"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
                    @ApiResponse(responseCode = "401", description = "Người dùng chưa được xác thực"),
                    @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
            }
    )
    @PutMapping("/private-info")
    public ResponseData<?> changePrivateInfo(
            @Valid @RequestBody ChangePrivateInfoRequest request
    ) {
        userService.updatePrivateInfo(request);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Cập nhật thông tin cá nhân thành công",
                LocalDateTime.now()
        );
    }


    @Operation(
            summary = "Cập nhật avatar người dùng - Cần access token",
            description = "API này cho phép người dùng cập nhật thông tin cá nhân của họ.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cập nhật thông tin thành công"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
                    @ApiResponse(responseCode = "401", description = "Người dùng chưa được xác thực"),
                    @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
            }
    )
    @PutMapping("/avatar")
    public ResponseData<?> updateAvatar(@RequestPart("avatar") MultipartFile avatarFile) {
            userService.updateAvatar(avatarFile);
            return new ResponseData<>(
                    HttpStatus.OK.value(),
                    "Cập nhật avatar thành công",
                    LocalDateTime.now()
            );
    }

}
