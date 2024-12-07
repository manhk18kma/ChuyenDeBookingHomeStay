package KMA.BeBookingApp.app.api.user;

import KMA.BeBookingApp.app.service.RateLimitOption;
import KMA.BeBookingApp.app.service.RateLimitResponse;
import KMA.BeBookingApp.app.service.RateLimiterService;
import KMA.BeBookingApp.domain.common.dto.ResponseData;
import KMA.BeBookingApp.domain.common.enumType.homestay.HomestayStatus;
import KMA.BeBookingApp.domain.common.enumType.user.TokenType;
import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import KMA.BeBookingApp.domain.common.exception.AppException;
import KMA.BeBookingApp.domain.common.validation.EnumValue;
import KMA.BeBookingApp.domain.user.dto.request.*;
import KMA.BeBookingApp.domain.user.dto.response.FailedAttemptsResponse;
import KMA.BeBookingApp.domain.user.dto.response.TokenResponse;
import KMA.BeBookingApp.domain.user.service.abtract.AuthService;
import KMA.BeBookingApp.domain.user.service.abtract.UserService;
import com.cloudinary.api.RateLimit;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "APIs for authentication and user management")
public class AuthController {
    UserService userService;
    AuthService authService;
    RateLimiterService rateLimiterService;



    @Operation(summary = "Đăng nhập với google code",
            description = """
                    Gửi lên google code từ client request from goole
                    Chú ý mỗi ip chỉ được request vào api này 5 lần 1 giờ 
                    """)
    @PostMapping("/outbound/authentication")
    public ResponseData<?> outboundAuthenticateWeb(
            @Parameter(description = "Mã xác thực dùng để đăng ký người dùng", required = true)
            @RequestParam(value = "code" ,required = false) String code,

            @Parameter(description = "Body yêu cầu chứa thông tin đổi token", required = true)
            @RequestBody @Valid OutBoundLoginRequest request,

            HttpServletRequest httpServletRequest) {

        boolean isAllowed = rateLimiterService.isAllowed(httpServletRequest , RateLimitOption.OUT_BOUND);
        if (!isAllowed) {
            throw new AppException(AppErrorCode.TOO_MANY_REQUEST);
        }

        var response = userService.outboundAuthenticate(code, request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Outbound Login",
                LocalDateTime.now(),
                response
        );
    }




    @Operation(summary = "Đăng nhập người dùng",
            description = """
                    Đăng nhập bằng tài khoản mật khẩu init , nếu tài khoản đó đã login sai 5 lần trước đó thì cần captcha token
                    Có api cho việc kiểm tra số lần login sai
                    """)
    @PostMapping("/login")
    public ResponseData<TokenResponse> login(
            @Parameter(description = "Body yêu cầu đăng nhập chứa email và mật khẩu", required = true)
            @RequestBody @Valid LoginRequest request, HttpServletRequest httpServletRequest) {
        boolean isAllowed = rateLimiterService.isAllowed(httpServletRequest , RateLimitOption.LOG_IN);
        TokenResponse response = authService.login(request,isAllowed);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Đăng nhập thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Đăng xuất", description = "Logout , gửi lên accessToken và refreshToken để invalid")
    @PostMapping("/logout")
    public ResponseData<?> logout(
            @Parameter(description = "Body yêu cầu chứa refresh token để đăng xuất", required = true)
            @RequestBody @Valid LogoutRequest request) throws ParseException, JOSEException {
        authService.logout(request);
        return new ResponseData<>(
                HttpStatus.NO_CONTENT.value(),
                "Đăng xuất thành công",
                LocalDateTime.now()
        );
    }

    @Operation(summary = "Làm mới token", description = "Lấy accessToken bằng refreshToken..")
    @PostMapping("/tokens-refresh")
    public ResponseData<TokenResponse> refreshToken(
            @Parameter(description = "Body yêu cầu chứa refresh token để lấy token truy cập mới", required = true)
            @RequestBody @Valid RefreshTokenRequest request) throws ParseException, JOSEException {
        TokenResponse response = authService.refreshToken(request);
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "Làm mới token thành công",
                LocalDateTime.now(),
                response
        );
    }

    @Operation(summary = "Kiểm tra token", description = "Api này để test")
    @PostMapping("/tokens-introspect")
    public ResponseData<Boolean> introspectToken(
            @Parameter(description = "Body yêu cầu chứa token để kiểm tra", required = true)
            @RequestBody IntrospectRequest request,
            HttpServletRequest httpServletRequest) throws ParseException, JOSEException {
        System.out.println(httpServletRequest.getRemoteAddr());
        boolean isValid = authService.introspect(request.getToken(), TokenType.RESET_TOKEN);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Token đã được kiểm tra thành công",
                LocalDateTime.now(),
                isValid
        );
    }

    @Operation(summary = "Kiểm tra token", description = "Api này để test")
    @PostMapping("/tokens-introspect-captcha")
    public ResponseData<Boolean> introspectCaptcha(
            @Parameter(description = "Body yêu cầu chứa token để kiểm tra", required = true)
            @RequestBody IntrospectRequest request,
            HttpServletRequest httpServletRequest) throws ParseException, JOSEException {
        System.out.println(httpServletRequest.getRemoteAddr());
        boolean isValid = authService.introspectCaptcha(request);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Captcha đã được kiểm tra thành công",
                LocalDateTime.now(),
                isValid
        );
    }


    @Operation(summary = "Bước 1: Thay đổi mật khẩu", description = "Gửi liên kết đặt lại mật khẩu đến địa chỉ email của người dùng. Nếu đã request trước đó quá 5 lần rồi thì bắt buộc phải có captcha")
    @PostMapping("/init-change-password")
    public ResponseData<Void> initChangePassword(
            @Parameter(description = "Body yêu cầu chứa email để khôi phục mật khẩu", required = true)
            @RequestBody @Valid InitChangePasswordRequest request, HttpServletRequest httpServletRequest) throws ParseException, JOSEException {
        boolean isAllowed = rateLimiterService.isAllowed(httpServletRequest , RateLimitOption.CHANGE_PASSWORD);
        authService.initChangePassword(request , isAllowed);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Vui lòng kiểm tra email của bạn để nhận hướng dẫn đặt lại mật khẩu.",
                LocalDateTime.now()
        );
    }

    @Operation(summary = "Bước 2: Đặt lại mật khẩu", description = """
            Đặt lại mật khẩu của người dùng bằng cách sử dụng token reset và mật khẩu mới. Token reset sẽ  gửi vào mail của ngừoi dùng
            """)
    @PostMapping("/change-password")
    public ResponseData<TokenResponse> changePassword(
            @Parameter(description = "Body yêu cầu chứa token reset và mật khẩu mới", required = true)
            @RequestBody @Valid ChangePasswordRequest request) throws ParseException, JOSEException {
        TokenResponse response = authService.changePassword(request);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Đặt lại mật khẩu thành công",
                LocalDateTime.now(),
                response
        );
    }


    @Operation(
            summary = "Lấy số lần request limit của ip address",
            description = "Lấy thông tin về số lần request giới hạn của một user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lấy thành công số lần request."),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy thông tin người dùng."),
                    @ApiResponse(responseCode = "500", description = "Lỗi server nội bộ.")
            }
    )
    @GetMapping("/rate-limiter")
    public ResponseData<?> getFailedAttemptsOfUser(HttpServletRequest httpServletRequest ,
                                                   @RequestParam(defaultValue = "LOG_IN") @EnumValue(name = "type_api", enumClass = RateLimitOption.class) String typeApi) {
        RateLimitResponse response = rateLimiterService.getRateLimit(httpServletRequest , typeApi);
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "Lấy số lần request giới hạn thành công.",
                LocalDateTime.now(),
                response
        );
    }
}
