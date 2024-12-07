package KMA.BeBookingApp.domain.user.service.impl;

import KMA.BeBookingApp.domain.common.enumType.user.TokenType;
import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import KMA.BeBookingApp.domain.common.exception.AppException;
import KMA.BeBookingApp.domain.notification.service.abtract.MailService;
import KMA.BeBookingApp.domain.user.dto.request.*;
import KMA.BeBookingApp.domain.user.dto.response.ExchangeTokenResponse;
import KMA.BeBookingApp.domain.user.dto.response.OutboundUserResponse;
import KMA.BeBookingApp.domain.user.dto.response.TokenResponse;
import KMA.BeBookingApp.domain.user.entity.ChangePasswordToken;
import KMA.BeBookingApp.domain.user.entity.InvalidatedToken;
import KMA.BeBookingApp.domain.user.entity.User;
import KMA.BeBookingApp.domain.user.repository.ChangePasswordTokenRepository;
import KMA.BeBookingApp.domain.user.repository.InvalidatedTokenRepository;
import KMA.BeBookingApp.domain.user.repository.UserRepository;
import KMA.BeBookingApp.domain.user.repository.httpclient.OutboundIdentityClient;
import KMA.BeBookingApp.domain.user.repository.httpclient.OutboundUserClient;
import KMA.BeBookingApp.domain.user.service.abtract.AuthService;
import KMA.BeBookingApp.domain.user.service.abtract.TokenService;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class AuthServiceImpl implements AuthService {
    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI;

    @NonFinal
    protected final String GRANT_TYPE = "authorization_code";

    @NonFinal
    @Value("${recaptcha.secret-key}")
    private String secretKey;
    @NonFinal
    @Value("${recaptcha.verify-url}")
    private  String VERIFY_URL;

    private static final int MAX_FAILED_ATTEMPTS = 5;


    OutboundIdentityClient outboundIdentityClient;
    OutboundUserClient outboundUserClient;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    TokenService tokenService;
    InvalidatedTokenRepository invalidatedTokenRepository;
    ChangePasswordTokenRepository changePasswordTokenRepository;
    MailService mailService;
    RestTemplate restTemplate;


    @Override
    public OutboundUserResponse verifyOauth2GoogleWeb(String code) {
        try {
            ExchangeTokenResponse response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                    .code(code)
                    .clientId(CLIENT_ID)
                    .clientSecret(CLIENT_SECRET)
                    .redirectUri(REDIRECT_URI)
                    .grantType(GRANT_TYPE)
                    .build());

            log.info("TOKEN RESPONSE {}", response);
            OutboundUserResponse userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());
            return userInfo;
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    public OutboundUserResponse verifyOauth2GoogleIos(String idToken) {
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                return OutboundUserResponse.builder()
                        .id((String) body.get("sub"))
                        .email((String) body.get("email"))
                        .emailVerified(Boolean.parseBoolean(body.get("email_verified").toString()))
                        .name((String) body.get("name"))
                        .givenName((String) body.get("given_name"))
                        .familyName((String) body.get("family_name"))
                        .picture((String) body.get("picture"))
                        .build();
            } else {
                throw new AppException(AppErrorCode.UNAUTHENTICATED);
            }
        } catch (Exception e) {
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        }
    }


    @Override
    public OutboundUserResponse verifyOauth2GoogleAndroid(String idToken) {
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();

                return OutboundUserResponse.builder()
                        .id((String) body.get("sub"))
                        .email((String) body.get("email"))
                        .emailVerified(Boolean.parseBoolean(body.get("email_verified").toString()))
                        .name((String) body.get("name"))
                        .givenName((String) body.get("given_name"))
                        .familyName((String) body.get("family_name"))
                        .picture((String) body.get("picture"))
                        .locale((String) body.get("locale"))
                        .build();
            }
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        } catch (RestClientException e) {
            log.error("Error verifying Google OAuth2 token for Android: {}", e.getMessage(), e);
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    public boolean introspectCaptcha(IntrospectRequest request) {
        return submitCaptcha(request.getToken());
    }


    @Override
    public TokenResponse login(LoginRequest request , boolean isAllowed) {
        User user = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_EXISTED));

        if (user.getFailedAttempts() > MAX_FAILED_ATTEMPTS || !isAllowed) {
            boolean isCaptchaValid = submitCaptcha(request.getCaptcha());
            if (!isCaptchaValid) {
                log.warn("Failed login attempt for user: {}", user.getUsername());
                throw new AppException(AppErrorCode.UNAUTHENTICATED);
            }
        }

        if (!user.getIsSetPassword()) {
            incrementFailedAttempts(user);
            throw new AppException(AppErrorCode.INVALID_USERNAME_PASSWORD);
        }



        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            log.warn("Failed login attempt for user: {}", user.getUsername());
            incrementFailedAttempts(user);
            throw new AppException(AppErrorCode.INVALID_USERNAME_PASSWORD);
        }

        user.setFailedAttempts(0);
        user.setTokenDevice(request.getTokenDevice());
        userRepository.save(user);

        String accessToken = tokenService.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = tokenService.generateToken(user, TokenType.REFRESH_TOKEN);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementFailedAttempts(User user) {
        user.setFailedAttempts(user.getFailedAttempts() + 1);
        System.out.println(userRepository.save(user).getFailedAttempts());
    }


    @Override
    public String getAuthenticationName() {
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();
        if (authentication == null || !(authentication instanceof JwtAuthenticationToken)) {
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        }
        String name = authentication.getName();
        return name;
    }



    @Override
    @Transactional
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        var signedRefreshJWT = tokenService.verifyToken(request.getRefreshToken(), TokenType.REFRESH_TOKEN);
        String email = null;
        try {
            email = signedRefreshJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        }
        var user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(AppErrorCode.UNAUTHENTICATED));
        var accessToken = tokenService.generateToken(user , TokenType.ACCESS_TOKEN);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(request.getRefreshToken())
                .build();
    }

    @Override
    @Transactional

    public void logout(LogoutRequest request) {
        boolean accessTokenInvalidated = false;
        boolean refreshTokenInvalidated = false;
        User user = validateAndGetUserByAuthenticationName();
        user.setTokenDevice("");
        userRepository.save(user);
        try {
            invalidateToken(request.getAccessToken(), TokenType.ACCESS_TOKEN);
            accessTokenInvalidated = true;
        } catch (AppException ignored) {
        }
        try {
            invalidateToken(request.getRefreshToken(), TokenType.REFRESH_TOKEN);
            refreshTokenInvalidated = true;
        } catch (AppException ignored) {
        }
        if (accessTokenInvalidated || refreshTokenInvalidated) {
        }
    }

    @Override
    @Transactional

    public void initChangePassword(InitChangePasswordRequest request , boolean isAllowedRateLimit) {
        System.out.println(request.getCaptcha());
        if(!isAllowedRateLimit){
            boolean isCaptchaValid = submitCaptcha(request.getCaptcha());
            if (!isCaptchaValid) {
                log.warn("isCaptchaValid for user: {}", request.getEmail());
                throw new AppException(AppErrorCode.UNAUTHENTICATED);
            }
        }
        String email = request.getEmail();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_EXISTED));

        String resetToken = tokenService.generateToken(user, TokenType.RESET_TOKEN);
        SignedJWT signedJWT = tokenService.verifyToken(resetToken, TokenType.RESET_TOKEN);

        changePasswordTokenRepository.disableAllTokensForEmail(user.getEmail());

        ChangePasswordToken changePasswordToken = null;
        try {
            changePasswordToken = ChangePasswordToken.builder()
                    .isAble(true)
                    .email(user.getEmail())
                    .jwtId(signedJWT.getJWTClaimsSet().getJWTID())
                    .build();
        } catch (ParseException e) {
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        }
        changePasswordTokenRepository.save(changePasswordToken);
        mailService.sendResetPasswordEmail( resetToken ,user.getEmail());

    }

    @Override
    @Transactional

    public TokenResponse changePassword(ChangePasswordRequest request) {
        SignedJWT signedJWT = tokenService.verifyToken(request.getToken(), TokenType.RESET_TOKEN);
        String jwtId = null;
        try {
            jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        } catch (ParseException e) {
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        }

        ChangePasswordToken changePasswordToken = changePasswordTokenRepository
                .findChangePasswordTokenAble(jwtId)
                .orElseThrow(() -> new AppException(AppErrorCode.UNAUTHENTICATED));

        User user = userRepository.findByEmail(changePasswordToken.getEmail())
                .orElseThrow(() -> new AppException(AppErrorCode.USER_NOT_EXISTED));

        if (!validatePassword(user , request)) {
            throw new AppException(AppErrorCode.PASSWORDS_INVALID);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsSetPassword(true);
        user.setTokenDevice(request.getTokenDevice());
        user.setTokenVersion(UUID.randomUUID().toString());
        changePasswordToken.setIsAble(false);

        userRepository.save(user);
        changePasswordTokenRepository.save(changePasswordToken);


        String accessToken = tokenService.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = tokenService.generateToken(user, TokenType.REFRESH_TOKEN);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
//    @Transactional
    public boolean introspect(String token, TokenType tokenType) {
        boolean isValid = true;
        try {
            tokenService.verifyToken(token, tokenType);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public User validateAndGetUserByAuthenticationName() {
        String email = getAuthenticationName();
        User user = userRepository.validateAndGetUserByAuthenticationName(email)
                .orElseThrow(()->new AppException(AppErrorCode.UNAUTHENTICATED));
        return user;
    }



    //helper function
    public void invalidateToken(String token, TokenType tokenType) {
        SignedJWT signedJWT = tokenService.verifyToken(token, tokenType);
        InvalidatedToken invalidatedToken = null;
        try {
            invalidatedToken = InvalidatedToken.builder()
                    .tokenType(tokenType)
                    .jwtId(signedJWT.getJWTClaimsSet().getJWTID())
                    .build();
        } catch (ParseException e) {
            throw new AppException(AppErrorCode.UNAUTHENTICATED);
        }
        invalidatedTokenRepository.save(invalidatedToken);
    }

    public boolean submitCaptcha(String captchaToken) {
        try {
            String url = String.format("%s?secret=%s&response=%s", VERIFY_URL, secretKey, captchaToken);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            System.out.println(responseBody);

            return responseBody != null && responseBody.contains("\"success\": true");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    private boolean validatePassword(User user, ChangePasswordRequest request) {
        if (user.getUsername().equals(request.getPassword())) {
            return false;
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return false;
        }

        String password = request.getPassword();
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }

        int matchGroups = 0;

        if (Pattern.compile("[A-Z]").matcher(password).find()) {
            matchGroups++;
        }

        if (Pattern.compile("[a-z]").matcher(password).find()) {
            matchGroups++;
        }

        if (Pattern.compile("[0-9]").matcher(password).find()) {
            matchGroups++;
        }

        if (Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find()) {
            matchGroups++;
        }

        return matchGroups >= 2;
    }

}


//    Chính sách mật khẩu an toàn
//    Để đảm bảo an toàn thông tin và bảo mật tài khoản người dùng, các mật khẩu cần tuân thủ một chính sách rõ ràng, bao gồm các yêu cầu về độ dài, tính đa dạng của các nhóm ký tự và các quy định bổ sung. Dưới đây là một chính sách mật khẩu chuẩn mà bạn có thể áp dụng:
//
//        1. Độ dài mật khẩu
//        Tối thiểu: 8 ký tự
//        Tối đa: 20 ký tự
//        Mật khẩu không được quá ngắn để tránh dễ dàng bị dò tìm và cũng không nên quá dài để tránh khó nhớ.
//
//        2. Yêu cầu về các nhóm ký tự
//        Mật khẩu phải bao gồm ít nhất 2 trong 4 nhóm ký tự sau:
//
//        Chữ hoa (A-Z): Đảm bảo mật khẩu không chỉ chứa chữ thường.
//        Chữ thường (a-z): Đảm bảo mật khẩu không chỉ chứa chữ hoa.
//        Số (0-9): Đảm bảo mật khẩu có sự kết hợp của các ký tự số.
//        Ký tự đặc biệt (!@#$%^&*(),.?":{}|<>[]): Mật khẩu cần có ít nhất một ký tự đặc biệt để tăng độ phức tạp.
//        Mục tiêu của việc yêu cầu ít nhất 2 nhóm là tạo ra mật khẩu khó đoán và tăng tính bảo mật của hệ thống.
//
//        3. Các điều kiện bổ sung
//        Không trùng với tên người dùng: Mật khẩu không được giống tên người dùng hoặc bất kỳ thông tin cá nhân dễ đoán.
//        Khớp giữa mật khẩu và xác nhận mật khẩu: Mật khẩu phải khớp với mật khẩu xác nhận để tránh lỗi trong quá trình thay đổi mật khẩu.
//        4. Mật khẩu không được chứa thông tin cá nhân dễ đoán
//        Mật khẩu không được chứa các thông tin như tên, ngày sinh, hoặc các dữ liệu cá nhân dễ dàng tìm thấy trên các mạng xã hội hay các thông tin công khai.
//        5. Xác nhận mật khẩu
//        Khi thay đổi mật khẩu, yêu cầu người dùng nhập lại mật khẩu xác nhận để đảm bảo tính chính xác của mật khẩu.
//
